package com.reserve.illoomung.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "store_operating_hours",
        uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "day_of_week"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreOperatingHours { // 업체 영업시간

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hours_id")
    private Long hoursId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_operating_hours_store"))
    private Stores store;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 0=일요일, 1=월요일, ..., 6=토요일

    @Column(name = "is_open", nullable = false)
    @Builder.Default
    public Boolean isOpen = true;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    // 생성자, getter, setter 생략
}
