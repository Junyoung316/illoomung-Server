package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByAmenityNameIn(List<String> amenityNames);
    Optional<Amenity> findAllByAmenityId(Long id);

    @Query("SELECT COALESCE(MAX(a.sortOrder), 0) FROM Amenity a")
    Integer findMaxSortOrder();
}
