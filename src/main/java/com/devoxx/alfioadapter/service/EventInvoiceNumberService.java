package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.EventInvoiceNumber;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link EventInvoiceNumber}.
 */
public interface EventInvoiceNumberService {

    /**
     * Get all the invoiceNumbers.
     *
     * @return the list of entities.
     */
    Page<InvoiceNumberDTO> findAll(Pageable page);
}
