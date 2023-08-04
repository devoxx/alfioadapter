package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.domain.InvoiceHistory;
import com.devoxx.alfioadapter.service.dto.InvoiceHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devoxx.alfioadapter.domain.InvoiceHistory}.
 */
public interface InvoiceHistoryService {

    enum Action {
        GENERATE_INVOICE_NUMBER,
        CANCEL_INVOICE,
        CONFIRM_INVOICE,
        CANCEL_INVOICE_NOT_FOUND,
        CONFIRM_INVOICE_WRONG_ALF_IO_EVENT,
        CANCEL_INVOICE_WRONG_ALF_IO_EVENT,
        GET_INVOICE_NUMBER_WRONG_ALF_IO_EVENT
    }

    /**
     * Save a invoiceHistory.
     *
     * @param eventId   the event id
     * @param invoiceNumber the invoice number
     * @param action    the invoice history action
     * @return the persisted entity
     */
    InvoiceHistory save(Integer eventId, Integer invoiceNumber, Action action);

    /**
     * Get all the invoiceHistories.
     *
     * @return the list of entities.
     */
    Page<InvoiceHistoryDTO> findAll(Pageable pageable);
}
