package com.reserve.illoomung.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "login_attempts",
        indexes = {
        @Index(name = "idx_account_id", columnList = "accountId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id",  nullable = false, updatable = false)
    private Long id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id",
            foreignKey = @ForeignKey(name = "fk_login_account"))
    private Account account; // 상위 데이터(account) 데이터 삭제시 null로 변경 또는 유지

    @CreationTimestamp
    @Column(name = "attempted_at", updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant attemptedAt;

    @Column(name = "ip_address", columnDefinition = "Text")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "Text")
    private String userAgent;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "fail_reason")
    private String failReason;
}

// CREATE TABLE login_attempts ( -- 로그인 시도 기록 테이블
//                                      id BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '테이블 고유 식별 번호',
//                              account_id BIGINT UNSIGNED COMMENT '사용자 고유 식별 번호 (account 테이블) 존재하지 않는 사용자의 경우 NULL',
//                              attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '로그인 시도 시각',
//                              ip_address TEXT COMMENT '요청 ip 주소 암호문',
//                              user_agent TEXT COMMENT '요청 브라우저 정보 암호문',
//                              success BOOLEAN NOT NULL COMMENT '성공 여부 (TRUE: 성공, FALSE: 실패)',
//                              fail_reason VARCHAR(255) COMMENT '실패 사유 (예: WRONG_PASSWORD, NO_ACCOUNT)',
//
//INDEX idx_account_id (account_id), -- COMMENT '특정 사용자의 로그인 기록을 빠르게 조회하기 위한 인덱스',
//
//CONSTRAINT fk_login_account
//FOREIGN KEY (account_id)
//REFERENCES account(account_id)
//ON DELETE SET NULL -- COMMENT '외래 키 설정 (사용자 탈퇴 시 기록은 남기되, 연결만 끊음)'
//        );