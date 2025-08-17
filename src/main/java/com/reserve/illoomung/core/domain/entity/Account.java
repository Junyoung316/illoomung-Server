package com.reserve.illoomung.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.reserve.illoomung.core.util.Time;
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
    private Long accountId;

    @Lob
    @Column(name = "email")
    private String email;  // 암호화된 이메일

    @Column(name = "email_hash", length = 64, unique = true)
    private String emailHash;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;  // 소셜 로그인 시 null 가능

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider", nullable = false)
    @Builder.Default
    private SocialProvider socialProvider = SocialProvider.NONE;

    @Lob
    @Column(name = "social_id")
    private String socialId;

    @Column(name = "social_id_hash", length = 64)
    private String socialIdHash;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "dormant_at")
    private LocalDateTime dormantAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Time.krTime();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Time.krTime();
    }

    public void lastLoginAtUpdate() {
        this.lastLoginAt = Time.krTime();
    }
}

