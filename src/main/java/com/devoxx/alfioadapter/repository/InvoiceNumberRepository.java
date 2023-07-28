package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;


/**
 * Spring Data JPA repository for the InvoiceNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceNumberRepository extends JpaRepository<InvoiceNumber, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(i.invoiceNumber) FROM InvoiceNumber i WHERE i.eventId LIKE :eventId")
    Integer findHighestInvoiceNumberForUpdate(@Param("eventId") String eventId);

    @Query("SELECT count(i.invoiceNumber) FROM InvoiceNumber i WHERE i.eventId LIKE :eventId")
    Long countEventId(@Param("eventId") String eventId);

    InvoiceNumber findByInvoiceNumber(Integer invoiceNumber);
}
