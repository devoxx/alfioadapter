package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.RecycledInvoiceNumber;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link RecycledInvoiceNumber}.
 */
public interface RecyclableInvoiceNumberService {

    /**
     * Get all the recyclableInvoiceNumbers.
     *
     * @return the list of entities.
     */
    Page<RecyclableInvoiceNumberDTO> findAll(Pageable pageable);

    /**
     * Delete the "id" recyclableInvoiceNumber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
