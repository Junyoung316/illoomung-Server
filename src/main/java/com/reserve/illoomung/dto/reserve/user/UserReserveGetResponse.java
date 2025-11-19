package com.reserve.illoomung.dto.reserve.user;

import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReserveGetResponse {
    private Long reservationId;
    private Long storeId;
    private LocalDateTime reservationDate;
    private String reservationNote;
    private ReservationStatus  reservationStatus;
}
