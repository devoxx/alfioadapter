package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.service.EventInvoiceNumberService;
import com.devoxx.alfioadapter.repository.EventInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing InvoiceNumber.
 */
@Service
public class EventInvoiceNumberServiceImpl implements EventInvoiceNumberService {

    private final Logger log = LoggerFactory.getLogger(EventInvoiceNumberServiceImpl.class);

    private final EventInvoiceNumberRepository eventInvoiceNumberRepository;
    private final InvoiceNumberMapper invoiceNumberMapper;

    public EventInvoiceNumberServiceImpl(final EventInvoiceNumberRepository eventInvoiceNumberRepository,
                                         final InvoiceNumberMapper invoiceNumberMapper) {
        this.eventInvoiceNumberRepository = eventInvoiceNumberRepository;
        this.invoiceNumberMapper = invoiceNumberMapper;
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
        return eventInvoiceNumberRepository.findAll(pageable)
            .map(invoiceNumberMapper::toDto);
    }
}
