package com.reserve.illoomung.dto.reserve.user;

import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserReserveSaveRequest {

    private Long offeringId; // offering (엔티티) 대신 offeringId (Long)를 받음

    @NotNull(message = "예약 시간은 필수입니다.")
    @Future(message = "예약 시간은 현재 시각보다 이후여야 합니다.") // 과거 시간 예약 방지
    private LocalDateTime reservationDatetime;

    private String requestNote;

    private ReservationStatus reservationStatus;
}
