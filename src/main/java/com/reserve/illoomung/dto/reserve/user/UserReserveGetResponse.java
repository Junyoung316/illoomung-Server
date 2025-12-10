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
    private ReservationStore store;
    private ReservationProduct product;
    private LocalDateTime reservationDate;
    private String reservationNote;
    private ReservationStatus  reservationStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationStore {
        private Long storeId;
        private String storeName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationProduct {
        private Long productId;
        private String productName;
        private String price;

    }
}
