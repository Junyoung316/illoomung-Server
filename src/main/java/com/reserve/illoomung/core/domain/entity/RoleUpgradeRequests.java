package com.reserve.illoomung.core.domain.entity;

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
    @Column(name = "request_id", nullable = false, updatable = false)
    private Long requestId;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_account"))
    private Account account;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private RoleStatus status = RoleStatus.PENDING;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id",
            foreignKey = @ForeignKey(name = "fk_admin_account"))
    private Account admin;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason; // 상태가 'REJECTED'일 경우에만 기록

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "processed_at", insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant processedAt;

}
