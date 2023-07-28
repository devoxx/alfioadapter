package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Spring Data JPA repository for the RecyclableInvoiceNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecyclableInvoiceNumberRepository extends JpaRepository<RecyclableInvoiceNumber, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RecyclableInvoiceNumber r WHERE r.eventId = :eventId ORDER BY r.invoiceNumber ASC")
    Optional<RecyclableInvoiceNumber> findLowestForUpdate(@Param("eventId") String eventId);

    @Query("SELECT COUNT(r.invoiceNumber) FROM RecyclableInvoiceNumber r WHERE r.eventId LIKE :eventId")
    Long countEventId(@Param("eventId") String eventId);
}
