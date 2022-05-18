package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
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

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing RecyclableInvoiceNumber.
 */
@Service
@Transactional
public class RecyclableInvoiceNumberServiceImpl implements RecyclableInvoiceNumberService {

    private final Logger log = LoggerFactory.getLogger(RecyclableInvoiceNumberServiceImpl.class);

    private final RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository;

    private final RecyclableInvoiceNumberMapper recyclableInvoiceNumberMapper;

    public RecyclableInvoiceNumberServiceImpl(RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository, RecyclableInvoiceNumberMapper recyclableInvoiceNumberMapper) {
        this.recyclableInvoiceNumberRepository = recyclableInvoiceNumberRepository;
        this.recyclableInvoiceNumberMapper = recyclableInvoiceNumberMapper;
    }

    /**
     * Save a recyclableInvoiceNumber.
     *
     * @param eventId the event identifier
     * @param invoiceNumber the invoice number to recycle
     * @return the persisted entity
     */
    @Override
    public RecyclableInvoiceNumber save(final String eventId,
                                        final Integer invoiceNumber) {

        log.debug("Request to save RecyclableInvoiceNumber : {}", invoiceNumber);

        final RecyclableInvoiceNumber recyclableInvoiceNumber = new RecyclableInvoiceNumber();
        recyclableInvoiceNumber.setCreationDate(ZonedDateTime.now());
        recyclableInvoiceNumber.setEventId(eventId);
        recyclableInvoiceNumber.setInvoiceNumber(invoiceNumber);

        return recyclableInvoiceNumberRepository.save(recyclableInvoiceNumber);
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
        return recyclableInvoiceNumberRepository.findAll(pageable)
            .map(recyclableInvoiceNumberMapper::toDto);
    }

    /**
     * Get one recyclableInvoiceNumber by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecyclableInvoiceNumberDTO> findOne(Long id) {
        log.debug("Request to get RecyclableInvoiceNumber : {}", id);
        return recyclableInvoiceNumberRepository.findById(id)
            .map(recyclableInvoiceNumberMapper::toDto);
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
