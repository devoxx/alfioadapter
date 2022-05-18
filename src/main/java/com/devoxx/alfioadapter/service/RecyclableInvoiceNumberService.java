package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber}.
 */
public interface RecyclableInvoiceNumberService {

    /**
     * Save a recyclableInvoiceNumber.
     *
     * @param eventId the event identifier
     * @param invoiceNumber the invoice number
     * @return the persisted entity
     */
    RecyclableInvoiceNumber save(String eventId, Integer invoiceNumber);

    /**
     * Get all the recyclableInvoiceNumbers.
     *
     * @return the list of entities.
     */
    Page<RecyclableInvoiceNumberDTO> findAll(Pageable pageable);

    /**
     * Get the "id" recyclableInvoiceNumber.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecyclableInvoiceNumberDTO> findOne(Long id);

    /**
     * Delete the "id" recyclableInvoiceNumber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
