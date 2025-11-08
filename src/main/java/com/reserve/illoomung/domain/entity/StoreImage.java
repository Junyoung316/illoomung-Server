package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.domain.entity.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "store_images",
        indexes = {@Index(name = "idx_store_type", columnList = "store_id, image_type")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreImage { // 업체 이미지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_image_store"))
    private Stores store;

    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", length = 20, nullable = false)
    @Builder.Default
    private ImageType imageType = ImageType.OTHER;

    @Column(name = "alt_text", length = 200)
    private String altText;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant uploadedAt;

    // 생성자, getter, setter 생략
}