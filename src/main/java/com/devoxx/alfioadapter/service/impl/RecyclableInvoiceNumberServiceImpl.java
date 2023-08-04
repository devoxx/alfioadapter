package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.RecyclableInvoiceNumberService;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.RecyclableInvoiceNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing RecyclableInvoiceNumber.
 */
@Service
@Transactional
public class RecyclableInvoiceNumberServiceImpl implements RecyclableInvoiceNumberService {

    private final Logger log = LoggerFactory.getLogger(RecyclableInvoiceNumberServiceImpl.class);

    private final RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository;

    public RecyclableInvoiceNumberServiceImpl(RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository) {
        this.recyclableInvoiceNumberRepository = recyclableInvoiceNumberRepository;
    }

    /**
     * Get all the recyclableInvoiceNumbers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RecyclableInvoiceNumberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RecyclableInvoiceNumbers");
//        return recyclableInvoiceNumberRepository.findAll(pageable)
//            .map(recyclableInvoiceNumberMapper::);
        return null;
    }

    /**
     * Delete the recyclableInvoiceNumber by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecyclableInvoiceNumber : {}", id);
        recyclableInvoiceNumberRepository.deleteById(id);
    }
}
