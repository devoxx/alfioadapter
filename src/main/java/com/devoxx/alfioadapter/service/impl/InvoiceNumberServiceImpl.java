package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import com.devoxx.alfioadapter.web.rest.errors.MaxInvoiceNumberReachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PessimisticLockException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing InvoiceNumber.
 */
@Service
@Transactional
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
    @Transactional
    @Override
    public Integer nextInvoiceNumber(final String eventId) {
        log.debug("Next invoice number for event {}", eventId);

        Integer invoiceNumber = 1;

        try {
            if (recyclableInvoiceNumberRepository.countEventId(eventId) > 0) {
                Optional<RecyclableInvoiceNumber> recyclableInvoiceNr = recyclableInvoiceNumberRepository.findLowestForUpdate(eventId);

                if (recyclableInvoiceNr.isPresent()) {
                    invoiceNumber = recyclableInvoiceNr.get().getInvoiceNumber();
                    recyclableInvoiceNumberRepository.deleteById(recyclableInvoiceNr.get().getId());
                }

            } else if (invoiceNumberRepository.countEventId(eventId) > 0) {
                invoiceNumber = invoiceNumberRepository.findHighestInvoiceNumberForUpdate(eventId);

                if (invoiceNumber == null || invoiceNumber == Integer.MAX_VALUE) {
                    throw new MaxInvoiceNumberReachedException();
                }
                invoiceNumber++;
            }

            saveInvoiceNumber(eventId, invoiceNumber);
            return invoiceNumber;

        } catch (PessimisticLockException e) {
            log.error("Could not get an invoice number due to a locking conflict", e);
            // handle the exception, you could decide to re-try, throw an application-specific exception, etc.
            throw new InvoiceNumberNotGeneratedException(e);
        }
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
