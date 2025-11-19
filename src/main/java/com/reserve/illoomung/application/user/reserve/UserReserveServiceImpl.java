package com.reserve.illoomung.application.user.reserve;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.StoreReservation;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import com.reserve.illoomung.domain.repository.StoreOfferingRepository;
import com.reserve.illoomung.domain.repository.StoreReservationRepository;
import com.reserve.illoomung.domain.repository.StoresRepository;
import com.reserve.illoomung.dto.reserve.user.UserReserveGetResponse;
import com.reserve.illoomung.dto.reserve.user.UserReserveSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReserveServiceImpl implements UserReserveService {

    private final AccountRepository accountRepository;

    private final StoresRepository storesRepository;
    private final StoreOfferingRepository storeOfferingRepository;
    private final StoreReservationRepository storeReservationRepository;

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
    public List<UserReserveGetResponse> getReserve() {
        Account account = userCheck();
        List<StoreReservation> reserve = storeReservationRepository.findAllByAccount(account)
                .orElseThrow(() -> new RuntimeException("예약 내역을 찾을 수 없습니다."));
        return reserve.stream()
                .map(entity -> UserReserveGetResponse.builder()
                        .reservationId(entity.getReservationId())
                        .storeId(entity.getStore().getStoreId())
                        .reservationDate(entity.getReservationDatetime())
                        .reservationNote(entity.getRequestNote())
                        .reservationStatus(entity.getStatus())
                        .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveReserve(UserReserveSaveRequest request, Long id) {

        Account account = userCheck();

        Stores store = storesRepository.findAllByStoreId(id)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        StoreOffering offering = storeOfferingRepository.findAllByOfferingId(request.getOfferingId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        StoreReservation reserve = StoreReservation.builder()
                .store(store)
                .offering(offering)
                .account(account)
                .reservationDatetime(request.getReservationDatetime())
                .requestNote(request.getRequestNote())
                .status(ReservationStatus.PENDING) // 대기 중 기본값
                .build();

        storeReservationRepository.save(reserve);
    }

    @Override
    @Transactional
    public void cancelReserve(Long storeId, Long reservation) {
        Account account = userCheck();

        Stores store = storesRepository.findAllByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        StoreReservation reserve = storeReservationRepository.findAllByReservationIdAndStoreAndAccount(reservation, store, account)
                .orElseThrow(() -> new RuntimeException("예약 내역을 찾을 수 없습니다."));

        reserve.patchReserveStatus(ReservationStatus.CANCELED);
    }

}
