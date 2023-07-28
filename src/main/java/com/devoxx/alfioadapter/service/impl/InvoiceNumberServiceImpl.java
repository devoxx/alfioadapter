package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * Service Implementation for managing InvoiceNumber.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InvoiceNumberServiceImpl implements InvoiceNumberService {

    private final Logger log = LoggerFactory.getLogger(InvoiceNumberServiceImpl.class);

    private final InvoiceNumberRepository invoiceNumberRepository;

    private final InvoiceNumberMapper invoiceNumberMapper;

    private final RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository;

    public InvoiceNumberServiceImpl(final RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository,
                                    final InvoiceNumberRepository invoiceNumberRepository,
                                    final InvoiceNumberMapper invoiceNumberMapper) {
        this.invoiceNumberRepository = invoiceNumberRepository;
        this.invoiceNumberMapper = invoiceNumberMapper;
        this.recyclableInvoiceNumberRepository = recyclableInvoiceNumberRepository;
    }

    /**
     * Save a invoiceNumber.
     *
     * @param invoiceNumberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceNumberDTO save(InvoiceNumberDTO invoiceNumberDTO) {
        log.debug("Request to save InvoiceNumber : {}", invoiceNumberDTO);
        InvoiceNumber invoiceNumber = invoiceNumberMapper.toEntity(invoiceNumberDTO);
        invoiceNumber = invoiceNumberRepository.save(invoiceNumber);
        return invoiceNumberMapper.toDto(invoiceNumber);
    }

    /**
     * Get all the invoiceNumbers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceNumberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceNumbers");
        return invoiceNumberRepository.findAll(pageable)
            .map(invoiceNumberMapper::toDto);
    }

    /**
     * Get the next invoice number.
     * @param eventId event identifier
     * @return next invoice number
     */
    @Override
    public Integer nextInvoiceNumber(final String eventId) {
        try {
            return this.recyclableInvoiceNumberRepository.findFirstByEventId(eventId, PageRequest.of(0, 1))
                .stream().findFirst()
                .map(recycledInvoiceNumber -> {
                    Integer recycledNum = recycledInvoiceNumber.getInvoiceNumber();
                    recyclableInvoiceNumberRepository.delete(recycledInvoiceNumber);
                    return recycledNum;
                })
                .orElseGet(() -> this.invoiceNumberRepository.findByIdForUpdate(eventId)
                    .map(this::createNewInvoice)
                    .orElseGet(() -> createFirstInvoice(eventId)));
        } catch (CannotAcquireLockException ex) {
            log.error("Could not get an invoice number due to a locking conflict", ex);
            throw new InvoiceNumberNotGeneratedException(ex);
        }
    }

    private Integer createFirstInvoice(String eventId) {
        InvoiceNumber newInvoice = new InvoiceNumber();
        newInvoice.setCreationDate(ZonedDateTime.now());
        newInvoice.setInvoiceNumber(1);
        newInvoice.setEventId(eventId);
        this.invoiceNumberRepository.save(newInvoice);
        return 1;
    }

    private Integer createNewInvoice(InvoiceNumber existingInvoice) {
        Integer newInvoiceNumber = existingInvoice.getInvoiceNumber() + 1;
        existingInvoice.setInvoiceNumber(newInvoiceNumber);
        this.invoiceNumberRepository.save(existingInvoice);
        return newInvoiceNumber;
    }
}
