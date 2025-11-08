package com.reserve.illoomung.application.business.storeInfo.product;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.Stores;
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

    @Override
    public List<StoreInfoResponse.products> getStoreProductsAll(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if(authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            log.info("authenticated id: {}", userId);

            Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            log.info("authenticated account: {}", account);

            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(id, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            List<StoreOffering> storeProduct = storeOfferingRepository.findByStoreStoreId(store.getStoreId());

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

    // TODO: 가게 조회 및 사용자 인증, 가게 상품 등록 로직

    @Override
    @Transactional
    public void saveStoreProduct(Long id, StoreInfoResponse.products product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            log.info("authenticated id: {}", userId);

            Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            log.info("authenticated account: {}", account);

            Stores store = storesRepository.findByStoreIdAndOwnerAccountId(id, account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("사용자의 가게를 찾을 수 없습니다."));

            StoreOffering storeOffering = StoreOffering.builder()
                    .store(store)
                    .offeringName(product.getProductName())
                    .description(product.getProductDescription())
                    .price(product.getProductPrice())
                    .build();

            storeOfferingRepository.save(storeOffering);
        }
    }
}
