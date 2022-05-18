package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

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

    @Override
    public Integer nextNumber(final String eventId) {

        log.debug("Next invoice number for event {}", eventId);

        Integer invoiceNumber = 1;

        if (recyclableInvoiceNumberRepository.countEventId(eventId) > 0) {
            log.debug("Taking a recycled number");

            final RecyclableInvoiceNumber recyclableInvoiceNr = recyclableInvoiceNumberRepository.findLowest(eventId);

            invoiceNumber = recyclableInvoiceNr.getInvoiceNumber();

            recyclableInvoiceNumberRepository.deleteById(recyclableInvoiceNr.getId());

        } else if (invoiceNumberRepository.countEventId(eventId) > 0) {

            log.debug("No recycle numbers found, lets get highest invoice number");

            invoiceNumber = invoiceNumberRepository.findHighestInvoiceNumber(eventId);

            log.debug("Invoice number found: {}", invoiceNumber);

            invoiceNumber++;
        }

        saveInvoiceNumber(eventId, invoiceNumber);
        return invoiceNumber;
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
