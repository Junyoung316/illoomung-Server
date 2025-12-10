package com.reserve.illoomung.application.business.storeInfo.product;

import com.reserve.illoomung.application.es.StoreSearchService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.util.autocomplete.application.AutocompleteService;
import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.enums.Status;
import com.reserve.illoomung.domain.repository.StoreOfferingRepository;
import com.reserve.illoomung.domain.repository.StoresRepository;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements StoreProductService {

    private final AccountRepository accountRepository; // 사업자 정보 조회

    private final StoresRepository storesRepository; // 가게 정보
    private final StoreOfferingRepository storeOfferingRepository; // 상품 정보

    private final StoreSearchService storeSearchService; // 상품 검색 바인딩
    private final AutocompleteService autocompleteService;

    private Account userCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if(authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            log.info("authenticated id: {}", userId);

            Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            log.info("authenticated account: {}", account);
            return account;
        }
        return null;
    }


    @Override
    public List<StoreInfoResponse.products> getStoreProductsAll(Long id) {
        Account account = userCheck();
        if(account != null) {
            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(id, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            List<StoreOffering> storeProduct = storeOfferingRepository.findByStoreStoreIdAndStatus(store.getStoreId(), Status.ACTIVE);

            return storeProduct.stream()
                    .map(entity -> StoreInfoResponse.products.builder()
                            .productsId(entity.getOfferingId())
                            .productName(entity.getOfferingName())
                            .productDescription(entity.getDescription())
                            .productPrice(entity.getPrice())
                            .build())
                    .toList();
        }
        return null;
    }


    @Override
    @Transactional
    public void saveStoreProduct(Long id, StoreInfoResponse.products product) {
        Account account = userCheck();
        if (account != null) {
            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(id, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            StoreOffering storeOffering = StoreOffering.builder()
                    .store(store)
                    .offeringName(product.getProductName())
                    .description(product.getProductDescription())
                    .price(product.getProductPrice())
                    .build();

            StoreOffering saveStoreOffering =  storeOfferingRepository.save(storeOffering);
            autocompleteService.indexKeyword(saveStoreOffering.getOfferingName());
            storeSearchService.addProductToStore(store.getStoreId(), saveStoreOffering.getOfferingId(), product);
        }
    }

    @Override
    @Transactional
    public void patchStoreProduct(Long storeId, Long productId, StoreInfoResponse.products product) {
        Account account = userCheck();
        if (account != null) {
            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(storeId, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            StoreOffering storeOffering = storeOfferingRepository.findAllByOfferingId(productId)
                    .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

            storeOffering.patchProduct(product);
            storeSearchService.updateProductInStore(store.getStoreId(), productId, product);
        }
    }

    @Override
    @Transactional
    public void deleteStoreProduct(Long storeId, Long productId) {
        Account account = userCheck();
        if (account != null) {
            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(storeId, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            StoreOffering storeOffering = storeOfferingRepository.findAllByOfferingId(productId)
                    .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

            storeOffering.deleteProduct();
            storeSearchService.removeProductFromStore(store.getStoreId(), productId);
        }
    }
}
