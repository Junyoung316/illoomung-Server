package com.reserve.illoomung.core.domain.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist", indexes = {
        @Index(name = "idx_expires_at", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    @Column(name = "blacklisted_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant blacklistedAt;

}
