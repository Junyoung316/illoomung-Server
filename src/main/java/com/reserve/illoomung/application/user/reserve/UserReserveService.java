package com.reserve.illoomung.application.user.reserve;

import com.reserve.illoomung.dto.reserve.user.UserReserveGetResponse;
import com.reserve.illoomung.dto.reserve.user.UserReserveSaveRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface UserReserveService {
    List<LocalDateTime> getStoreReserveTime(Long storeId);
    List<UserReserveGetResponse> getReserve();
    void saveReserve(UserReserveSaveRequest request, Long id);
    void cancelReserve(Long store, Long reservation);
}
