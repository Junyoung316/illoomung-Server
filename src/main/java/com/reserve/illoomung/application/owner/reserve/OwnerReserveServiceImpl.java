package com.reserve.illoomung.application.owner.reserve;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.UserProfile;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.UserProfileRepository;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.StoreReservation;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import com.reserve.illoomung.domain.repository.StoreOfferingRepository;
import com.reserve.illoomung.domain.repository.StoreReservationRepository;
import com.reserve.illoomung.domain.repository.StoresRepository;
import com.reserve.illoomung.dto.reserve.owner.OwnerGetReserveInfo;
import com.reserve.illoomung.dto.reserve.owner.OwnerGetReserveInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerReserveServiceImpl implements OwnerReserveService {

    private final SecurityUtil securityUtil;

    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;

    private final StoresRepository storesRepository;
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
    public OwnerGetReserveInfo getReserveInfos(Long storeId) {

        Account account = userCheck();

        Stores store = storesRepository.findByStoreIdAndOwnerAccountId(storeId, Objects.requireNonNull(account).getAccountId())
                .orElseThrow(() -> new RuntimeException("권한 없음"));

        List<StoreReservation> response = storeReservationRepository.findAllByStore(store)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        log.info("총 예약 개수: {}", response.size());

        List<OwnerGetReserveInfoResponse> reserveInfoResponses = response.stream()
                .map(entity -> {
                    OwnerGetReserveInfoResponse.ReservationTime time = OwnerGetReserveInfoResponse.ReservationTime.builder()
                            .reservationDatetime(entity.getReservationDatetime())
                            .reservationCreatedDatetime(entity.getCreatedAt())
                            .build();

                    UserProfile customer = userProfileRepository.findByAccount(entity.getAccount())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                    OwnerGetReserveInfoResponse.Customer user = OwnerGetReserveInfoResponse.Customer.builder()
                            .customerNickname(securityUtil.textDecrypt(customer.getNickName()))
                            .customerName(securityUtil.textDecrypt(customer.getName()))
                            .customerPhone(securityUtil.textDecrypt(customer.getPhone()))
                            .build();

                    OwnerGetReserveInfoResponse.Offering offering = OwnerGetReserveInfoResponse.Offering.builder()
                            .offeringName(entity.getOffering().getOfferingName())
                            .offeringPrice(entity.getOffering().getPrice())
                            .build();

                    return OwnerGetReserveInfoResponse.builder()
                            .reservationId(entity.getReservationId())
                            .reservationTime(time)
                            .reservationCustomer(user)
                            .reservationOffering(offering)
                            .reservationNote(entity.getRequestNote())
                            .reservationStatus(entity.getStatus())
                            .build();

                }).toList();

        return OwnerGetReserveInfo.builder()
                .ReserveCount(response.size())
                .Reserve(reserveInfoResponses)
                .build();
    }

    @Override
    @Transactional
    public void patchReserve(Long storeId, Long reservationId, ReservationStatus reservationStatus) {
        StoreReservation reserve = storeReservationRepository.findAllByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 내역을 찾을 수 없습니다."));

        if(reserve.getStore().getStoreId().equals(storeId)) {
            reserve.patchReserveStatus(reservationStatus);
        }
    }

}

// PENDING, // 대기 (기본)
// CONFIRMED, // 확정
// CANCELED, // 취소
// COMPLETED, // 완료
// NO_SHOW // 노쇼