package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.core.auditing.BaseTimeEntity;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다.
@Entity
@Table(name = "store_reservations", indexes = {
        // SQL의 인덱스를 여기에 정의합니다.
        @Index(name = "idx_store_datetime", columnList = "store_id, reservation_datetime"),
        @Index(name = "idx_account_id", columnList = "account_id")
})
public class StoreReservation extends BaseTimeEntity { // 3번 파일(BaseTimeEntity) 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "reservation_id")
    private Long reservationId; // reservation_id -> id (JPA 관례)

    // SQL의 store_id (FK) -> JPA의 @ManyToOne 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // NOT NULL
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store; // 스토어 id

    // SQL의 offering_id (FK)
    @ManyToOne(fetch = FetchType.LAZY) // NULL 허용
    @JoinColumn(name = "offering_id")
    private StoreOffering offering; // 상품 id

    // SQL의 account_id (FK)
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // NOT NULL
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // 예약자 id

    // SQL의 reservation_datetime
    @Column(name = "reservation_datetime", nullable = false)
    private LocalDateTime reservationDatetime; // 예약 시작 시간 1시간 고정

    // SQL의 request_note (TEXT)
    @Lob // TEXT 타입을 매핑할 때 사용합니다.
    @Column(name = "request_note")
    private String requestNote; // 추가 요청 사항 및 메모

    // SQL의 status (ENUM)
    // 예약 상태
    @Enumerated(EnumType.STRING) // Enum 이름을 문자열로 저장 (e.g., "PENDING")
    @Column(name = "status", nullable = false, length = 20) // ENUM 값 길이에 맞춰 조절
    @Builder.Default
    private ReservationStatus status = ReservationStatus.PENDING; // Java단에서 기본값 설정


    // BaseTimeEntity로 이동된 컬럼
    // @Column(name = "created_at")
    // private LocalDateTime createdAt;
    //
    // @Column(name = "updated_at")
    // private LocalDateTime updatedAt;

    public void patchReserveStatus(ReservationStatus status) {
        this.status = status;
    }



}