package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.EventInvoiceNumber;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the InvoiceNumber entity.
 */
@Repository
public interface EventInvoiceNumberRepository extends JpaRepository<EventInvoiceNumber, Long> {
}
