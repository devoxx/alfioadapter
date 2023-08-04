package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.RecycledInvoiceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecyclableInvoiceNumber entity.
 */
@Repository
public interface RecyclableInvoiceNumberRepository extends JpaRepository<RecycledInvoiceNumber, Long> {
}
