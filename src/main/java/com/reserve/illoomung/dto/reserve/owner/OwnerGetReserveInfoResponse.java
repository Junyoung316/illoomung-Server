package com.reserve.illoomung.dto.reserve.owner;


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
public class OwnerGetReserveInfoResponse {
    private Long reservationId;

    private ReservationTime reservationTime;

    private Customer reservationCustomer;

    private Offering reservationOffering;

    private String reservationNote;

    private ReservationStatus reservationStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationTime {
        private LocalDateTime reservationDatetime;
        private LocalDateTime reservationCreatedDatetime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Customer {
        private String customerNickname;
        private String customerName;
        private String customerPhone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Offering {
        private String offeringName;
        private String offeringPrice;
    }


}
