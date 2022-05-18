package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.InvoiceHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InvoiceHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, Long> {}
