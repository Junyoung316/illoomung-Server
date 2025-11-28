``` sql
CREATE TABLE store_reservations ( -- 사업장 예약 정보 테이블
    reservation_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '예약 고유 식별자 (PK)',
    
    store_id BIGINT UNSIGNED NOT NULL COMMENT '예약 대상 사업장의 고유 식별자 (FK)',
    offering_id BIGINT UNSIGNED COMMENT '예약한 상품/서비스의 식별자 (FK)',
    account_id BIGINT UNSIGNED NOT NULL COMMENT '예약한 사용자 계정 ID (account.account_id)',

    -- [수정] DATE와 TIME을 DATETIME 1개로 통합 (1시간 고정)
    reservation_datetime DATETIME NOT NULL COMMENT '예약 시작 시각 (종료 시각은 +1시간으로 고정)', 
    
    request_note TEXT COMMENT '추가 요청사항 및 메모',

    -- [이전 제안 유지] NO_SHOW 상태 추가
    status ENUM('PENDING', 'CONFIRMED', 'CANCELED', 'COMPLETED', 'NO_SHOW') NOT NULL DEFAULT 'PENDING' COMMENT '예약 상태',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '예약 생성 시각',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '예약 수정 시각',

    -- [수정] 인덱스 변경
    -- 이 인덱스로 1시간 중복 예약을 효율적으로 확인할 수 있습니다.
    INDEX idx_store_datetime (store_id, reservation_datetime) COMMENT '사업장 시간대별 조회/중복 확인용 인덱스',
    INDEX idx_account_id (account_id) COMMENT '사용자 예약 조회용 인덱스',

    -- 외래 키 (원안 유지)
    CONSTRAINT fk_reservation_store
        FOREIGN KEY (store_id)
        REFERENCES stores(store_id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_reservation_offering
        FOREIGN KEY (offering_id)
        REFERENCES store_offerings(offering_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_reservation_account
        FOREIGN KEY (account_id)
        REFERENCES account(account_id)
        ON DELETE CASCADE
);
```