package com.reserve.illoomung.core.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속하는 엔티티에게 컬럼만 물려줍니다.
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 리스너 등록
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티 생성 시각 자동 저장
    @Column(name = "created_at", updatable = false) // 생성 시간은 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티 수정 시각 자동 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}