package com.devoxx.alfioadapter.service.impl;

import com.devoxx.alfioadapter.service.InvoiceHistoryService;
import com.devoxx.alfioadapter.domain.InvoiceHistory;
import com.devoxx.alfioadapter.repository.InvoiceHistoryRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceHistoryDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * Service Implementation for managing InvoiceHistory.
 */
@Service
@Transactional
public class InvoiceHistoryServiceImpl implements InvoiceHistoryService {

    private final Logger log = LoggerFactory.getLogger(InvoiceHistoryServiceImpl.class);

    private final InvoiceHistoryRepository invoiceHistoryRepository;

    private final InvoiceHistoryMapper invoiceHistoryMapper;

    public InvoiceHistoryServiceImpl(InvoiceHistoryRepository invoiceHistoryRepository,
                                     InvoiceHistoryMapper invoiceHistoryMapper) {
        this.invoiceHistoryRepository = invoiceHistoryRepository;
        this.invoiceHistoryMapper = invoiceHistoryMapper;
    }

    /**
     * Save a invoiceHistory.
     *
     * @param eventId   the event id
     * @param invoiceNumber the invoice number
     * @param action the status
     * @return the persisted entity
     */
    @Override
    public InvoiceHistory save(String eventId, Integer invoiceNumber, Action action) {
        log.debug("Request to save InvoiceHistory : {}", invoiceNumber);

        final InvoiceHistory invoiceHistory = new InvoiceHistory();
        invoiceHistory.creationDate(ZonedDateTime.now());
        invoiceHistory.setEventId(eventId);
        invoiceHistory.setInvoiceNumber(invoiceNumber);
        invoiceHistory.setStatus(action.toString());

        log.debug("Save InvoiceHistory : {}", invoiceHistory);
        return invoiceHistoryRepository.save(invoiceHistory);
    }

    /**
     * Get all the invoiceHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceHistories");
        return invoiceHistoryRepository.findAll(pageable)
            .map(invoiceHistoryMapper::toDto);
    }
}
