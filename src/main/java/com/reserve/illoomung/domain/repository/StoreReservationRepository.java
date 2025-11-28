package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.domain.entity.StoreReservation;
import com.reserve.illoomung.domain.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreReservationRepository extends JpaRepository<StoreReservation, Long> {
    Optional<StoreReservation> findAllByReservationId(Long reservation);
    Optional<List<StoreReservation>> findAllByAccount(Account account);
    Optional<StoreReservation> findAllByReservationIdAndAccount(Long reservation, Account account);
    Optional<StoreReservation> findAllByReservationIdAndStoreAndAccount(Long reservation, Stores store, Account account);
    Optional<List<StoreReservation>> findAllByStore(Stores store);
}
