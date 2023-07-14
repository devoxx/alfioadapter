package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Delete the "id" invoiceNumber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Return next available invoice number.
     *
     * @param eventId event identifier
     * @return next available invoice number
     */
    Integer nextInvoiceNumber(String eventId);

    /**
     * Get invoiceNumber by number.
     *
     * @param invoiceNumber the invoice number
     * @return the entity
     */
    InvoiceNumber findByInvoiceNumber(Integer invoiceNumber);
}
