package com.reserve.illoomung.application.owner.reserve;

import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import com.reserve.illoomung.dto.reserve.owner.OwnerGetReserveInfoResponse;

import java.util.List;

public interface OwnerReserveService {
    // 가게 예약 전체 조회
    List<OwnerGetReserveInfoResponse> getReserveInfos(Long storeId);
    // 가게 예약 상태 변경(확정, 취소, 완료, 노쇼)
    void patchReserve(Long storeId, Long reservationId, ReservationStatus reservationStatus);
}