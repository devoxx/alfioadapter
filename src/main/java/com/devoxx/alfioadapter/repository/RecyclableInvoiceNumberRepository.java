package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Spring Data JPA repository for the RecyclableInvoiceNumber entity.
 */
@Repository
public interface RecyclableInvoiceNumberRepository extends JpaRepository<RecyclableInvoiceNumber, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RecyclableInvoiceNumber r where r.eventId = :eventId order by r.invoiceNumber")
    List<RecyclableInvoiceNumber> findFirstByEventId(@Param("eventId") String eventId, @Param("pageable") Pageable pageable);
}
