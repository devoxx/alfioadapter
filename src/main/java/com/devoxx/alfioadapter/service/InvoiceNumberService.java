package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.web.rest.InvoiceResource;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

/**
 * Service Interface for managing {@link com.devoxx.alfioadapter.domain.InvoiceNumber}.
 */
public interface InvoiceNumberService {
    /**
     * Save a invoiceNumber.
     *
     * @param invoiceNumberDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceNumberDTO save(InvoiceNumberDTO invoiceNumberDTO);

    /**
     * Get all the invoiceNumbers.
     *
     * @return the list of entities.
     */
    Page<InvoiceNumberDTO> findAll(Pageable page);

    /**
     * Return next available invoice number.
     *
     * @param eventId event identifier
     * @return next available invoice number
     */
    Integer nextInvoiceNumber(String eventId);

    /**
     * Create init invoice with invoice number equal to zero.
     * @param eventId event identifier
     * @return invoice number
     */
    int createInitInvoice(String eventId);
}
