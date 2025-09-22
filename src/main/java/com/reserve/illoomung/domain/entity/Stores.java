package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.domain.entity.enums.StoreStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "stores",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = {"business_entity_id", "store_name"})
//        },
        indexes = {
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_address", columnList = "address_sido,address_sigungu"),
                @Index(name = "idx_sigungu", columnList = "address_sigungu")
        }
)
public class Stores { // 업체 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId; // 고유 식별자

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "business_entity_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_store_business_entity")
//    )
//    private BusinessEntity businessEntity;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "owner_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_store_owner")
//    )
//    private Account owner;

    @Column(name = "store_name", length = 100, nullable = false)
    private String storeName; // 가게 이름

    @Column(name = "phone", columnDefinition = "TEXT")
    private String phone; // 가게 전화번호

    @Column(name = "address_full", columnDefinition = "TEXT", nullable = false)
    private String addressFull; // 전체 주소 암호문

    @Column(name = "address_sido", length = 50, nullable = false)
    private String addressSido; // 시/도

    @Column(name = "address_sigungu", length = 50, nullable = false)
    private String addressSigungu; // 시/군/구

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 가게 설명

    @Column(name = "website_url", length = 200)
    private String websiteUrl; // 가게 홈페이지 url

    @Column(name = "instagram_url", length = 200)
    private String instagramUrl; // 가게 인스타 url

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StoreStatus status = StoreStatus.PENDING_APPROVAL; // 가게 상태(활성, 비활성, 일시 중지, 승인 보류) -> 기본: 승인 보류

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updatedAt;

}


// stores ( -- 사업장 정보 테이블

// store_id BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '사업장 고유 식별자 (PK)',
// business_entity_id BIGINT UNSIGNED NOT NULL COMMENT '사업체 고유 식별자 (FK)',
// owner_id BIGINT UNSIGNED NOT NULL COMMENT '이 사업장 플랫폼에 등록하고 관리하는 account_id (OWNER, FK)',
// store_name VARCHAR(100) NOT NULL COMMENT '사업장명(예: ooo oo점)',
// phone TEXT NOT NULL COMMENT '사업장 전화번호 암호문',
// address_full TEXT NOT NULL COMMENT '전체 주소 암호문',
// address_sido VARCHAR(50) NOT NULL COMMENT '시/도',
// address_sigungu VARCHAR(50) NOT NULL COMMENT '시/군/구',
// description TEXT COMMENT '사업장 설명',
// website_url VARCHAR(200) NULL COMMENT '사업장 홈페이지 URL(존재하지 않을 경우 NULL 가능)',
// instagram_url VARCHAR(200) NULL COMMENT '사업장 인스타그램 URL(존재하지 않을 경우 NULL 가능)',
// status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_APPROVAL') DEFAULT 'PENDING_APPROVAL' COMMENT '사업장 상태(활성, 비활성, 일시 중지, 승인 보류)',
// created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
// updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
//
// UNIQUE (business_entity_id, store_name), -- COMMENT '한 사업체 내에서 가게 이름은 고유해야 함',
// INDEX idx_status (status), -- COMMENT '특정 상태의 사업장을 빠르게 조회하기 위한 인덱스',
// INDEX idx_address (address_sido, address_sigungu), -- COMMENT '시/도 + 시/군/구 동시 검색 및 시/도 단독 검색을 위한 복합 인덱스',
// INDEX idx_sigungu (address_sigungu), -- COMMENT '시/군/구 단독 검색을 위한 인덱스',
//
// CONSTRAINT fk_store_business_entity
// FOREIGN KEY (business_entity_id)
// REFERENCES business_entities(business_entity_id)
// ON DELETE CASCADE,
//
// CONSTRAINT fk_store_owner
// FOREIGN KEY (owner_id)
// REFERENCES account(account_id)
// ON DELETE CASCADE