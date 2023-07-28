package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import com.devoxx.alfioadapter.web.rest.errors.MaxInvoiceNumberReachedException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PessimisticLockException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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

    private final Object lock = new Object();

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

    @NotNull
    private Integer createFirstInvoice(String eventId) {
        InvoiceNumber newInvoice = new InvoiceNumber();
        newInvoice.setCreationDate(ZonedDateTime.now());
        newInvoice.setInvoiceNumber(1);
        newInvoice.setEventId(eventId);
        this.invoiceNumberRepository.save(newInvoice);
        return 1;
    }

    @NotNull
    private Integer createNewInvoice(InvoiceNumber existingInvoice) {
        Integer newInvoiceNumber = existingInvoice.getInvoiceNumber() + 1;
        existingInvoice.setInvoiceNumber(newInvoiceNumber);
        this.invoiceNumberRepository.save(existingInvoice);
        return newInvoiceNumber;
    }

    private void saveInvoiceNumber(final String eventId, final Integer invoiceNumber) {
        final InvoiceNumber newInvoiceNumber = new InvoiceNumber();
        newInvoiceNumber.setCreationDate(ZonedDateTime.now());
        newInvoiceNumber.setEventId(eventId);
        newInvoiceNumber.setInvoiceNumber(invoiceNumber);
        invoiceNumberRepository.save(newInvoiceNumber);
    }

    /**
     * Get one invoiceNumber by id.
     *
     * @param invoiceNumber the invoice number to find
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceNumber findByInvoiceNumber(Integer invoiceNumber) {
        log.debug("Request to find InvoiceNumber : {}", invoiceNumber);
        return invoiceNumberRepository.findByInvoiceNumber(invoiceNumber);
    }


    /**
     * Delete the invoiceNumber by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceNumber : {}", id);
        invoiceNumberRepository.deleteById(id);
    }
}
