package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
