package com.reserve.illoomung.core.domain.entity;

import com.reserve.illoomung.core.auditing.BaseTimeEntity;
import com.reserve.illoomung.core.domain.entity.enums.RoleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Table(name = "role_upgrade_requests",
        indexes = {
                @Index(name = "idx_status", columnList = "status"),
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpgradeRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_Upgrade_request_id", nullable = false, updatable = false)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fkrole_Upgrade__user_account"))
    private Account account;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING) // enum 타입의 문자열을 저장, "EnumType.ORDINAL"로 지정 시 enum 순서 값(정수)가 저장
    @Builder.Default
    private RoleStatus status = RoleStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id",
            foreignKey = @ForeignKey(name = "fk_role_Upgrade_admin_account"))
    private Account admin;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason; // 상태가 'REJECTED'일 경우에만 기록

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "processed_at")
    private Instant processedAt;

}
