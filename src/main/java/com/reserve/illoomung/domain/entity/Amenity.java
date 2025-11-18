package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.core.auditing.BaseTimeEntity;
import com.reserve.illoomung.domain.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "amenities",
        uniqueConstraints = @UniqueConstraint(columnNames = "amenity_name"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Amenity extends BaseTimeEntity { // 편의시설

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "amenity_name", length = 100, nullable = false, unique = true)
    private String amenityName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url", length = 200)
    private String iconUrl;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    @Builder.Default
    private Status status = Status.ACTIVE;

//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false, insertable = false,
//            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private Instant createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at", insertable = false,
//            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private Instant updatedAt;

    public Amenity (String amenityName) {
        this.amenityName = amenityName;
    }

    // 생성자, getter, setter 생략
}