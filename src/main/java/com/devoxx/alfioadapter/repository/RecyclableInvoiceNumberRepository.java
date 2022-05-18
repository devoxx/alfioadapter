package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecyclableInvoiceNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecyclableInvoiceNumberRepository extends JpaRepository<RecyclableInvoiceNumber, Long> {

    @Query("select r from RecyclableInvoiceNumber r " +
        "WHERE r.invoiceNumber = (select min(r2.invoiceNumber) from RecyclableInvoiceNumber r2 where r2.eventId LIKE :eventId) ")
    RecyclableInvoiceNumber findLowest(@Param("eventId") String eventId);

    @Query("SELECT count(r.invoiceNumber) FROM RecyclableInvoiceNumber r WHERE r.eventId LIKE :eventId")
    Long countEventId(@Param("eventId") String eventId);
}
