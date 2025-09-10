package com.reserve.illoomung.core.domain.entity;

import com.reserve.illoomung.core.util.DateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;
import com.reserve.illoomung.core.domain.entity.enums.Status;

@Entity
@Table(name = "account",
       uniqueConstraints = @UniqueConstraint(name = "uk_provider_social_id", columnNames = {"social_provider", "social_id_hash"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false, updatable = false)
    private Long accountId; // 사용자 고유 번호

    @Lob
    @Column(name = "email")
    private String email;  // 이메일 암호

    @Column(name = "email_hash", length = 64, unique = true)
    private String emailHash; // 이메일 해시

    @Column(name = "password_hash")
    private String passwordHash;  // 비밀번호 해시, 소셜 로그인 시 null 가능

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER; // 사용자 권한

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider", nullable = false)
    @Builder.Default
    private SocialProvider socialProvider = SocialProvider.NONE; // 소셜 제공사

    @Lob
    @Column(name = "social_id")
    private String socialId; // 소셜 아이디 암호

    @Column(name = "social_id_hash", length = 64)
    private String socialIdHash; // 소셜 아이디 해시

    @Column(name = "last_login_at")
    private Instant lastLoginAt; // 마지막 로그인 시각

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE; // ('ACTIVE', 'INACTIVE', 'DORMANT', 'WITHDRAWN'), 활성 상태(활성화, 비활성화, 휴면, 탈퇴)

    @Column(name = "dormant_at")
    private Instant dormantAt; // 휴면 시각

    @Column(name = "withdrawn_at")
    private Instant withdrawnAt; // 탈퇴 사각

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt; // 생성 시각

    @Column(name = "updated_at")
    private Instant updatedAt; // 수정 시각

    @PrePersist
    protected void onCreate() {
        createdAt = DateTimeUtils.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = DateTimeUtils.now();
    }

    public void lastLoginAtUpdate() {
        this.lastLoginAt = DateTimeUtils.now();
    }
}

