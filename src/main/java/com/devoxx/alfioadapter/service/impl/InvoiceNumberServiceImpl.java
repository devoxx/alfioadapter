package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import com.devoxx.alfioadapter.web.rest.InvoiceResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
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
public class InvoiceNumberServiceImpl implements InvoiceNumberService {

    public static final int PAGE_ZERO = 0;
    public static final int ONE_ELEMENT = 1;
    private final Logger log = LoggerFactory.getLogger(InvoiceNumberServiceImpl.class);
    public static final int ZERO_INVOICE_NUMBER = PAGE_ZERO;
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
    @Transactional
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
    @NotNull
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer nextInvoiceNumber(final String eventId) {
        try {
            return this.recyclableInvoiceNumberRepository.findFirstByEventId(eventId, PageRequest.of(PAGE_ZERO, ONE_ELEMENT))
                .stream().findFirst()
                .map(recycledInvoiceNumber -> {
                    Integer recycledNum = recycledInvoiceNumber.getInvoiceNumber();
                    recyclableInvoiceNumberRepository.delete(recycledInvoiceNumber);
                    return recycledNum;
                })
                .orElseGet(() ->
                        this.invoiceNumberRepository.findByIdForUpdate(eventId)
                                                    .map(this::createNewInvoice)
                                                    .orElse(ZERO_INVOICE_NUMBER));
        } catch (CannotAcquireLockException ex) {
            log.error("Could not get an invoice number due to a locking conflict", ex);
            return ZERO_INVOICE_NUMBER;
        }
    }

    /**
     * Create a new invoice number.
     * @param existingInvoice existing invoice number
     * @return new invoice number
     */
    public Integer createNewInvoice(InvoiceNumber existingInvoice) {
        Integer newInvoiceNumber = existingInvoice.getInvoiceNumber() + ONE_ELEMENT;
        existingInvoice.setInvoiceNumber(newInvoiceNumber);
        try {
            this.invoiceNumberRepository.save(existingInvoice);
        } catch (DataIntegrityViolationException ex) {
            log.error("Could not create a new invoice number due to a data integrity violation", ex);
            return ZERO_INVOICE_NUMBER;
        }
        return newInvoiceNumber;
    }

    /**
     * Create a zero invoice number record so the invoice number increment logic works correctly.
     *
     * @param eventId event identifier
     * @return zero invoice number
     */
    @Transactional
    public int createInitInvoice(String eventId) {
        InvoiceNumberDTO invoiceNumberDTO = new InvoiceNumberDTO();
        invoiceNumberDTO.setInvoiceNumber(InvoiceResource.ZERO_INVOICE_NUMBER);
        invoiceNumberDTO.setCreationDate(ZonedDateTime.now());
        invoiceNumberDTO.setEventId(eventId);

        InvoiceNumberDTO savedInvoice = save(invoiceNumberDTO);
        return savedInvoice.getInvoiceNumber();
    }
}
