package com.devoxx.alfioadapter.web.rest;

import com.devoxx.alfioadapter.repository.InvoiceGeneratorRepository;
import com.devoxx.alfioadapter.service.EventInvoiceNumberService;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

/**
 * REST controller for managing InvoiceNumber.
 */
@RestController
@RequestMapping("/api")
public class InvoiceNumberResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceNumberResource.class);

    private final EventInvoiceNumberService eventInvoiceNumberService;
    private final InvoiceGeneratorRepository invoiceGeneratorRepository;

    InvoiceNumberResource(EventInvoiceNumberService eventInvoiceNumberService,
                          InvoiceGeneratorRepository invoiceGeneratorRepository) {
        this.eventInvoiceNumberService = eventInvoiceNumberService;
        this.invoiceGeneratorRepository = invoiceGeneratorRepository;
    }

    /**
     * GET /invoice-numbers: get all the invoiceNumbers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceNumbers in body
     */
    @GetMapping("/invoice-numbers")
    public ResponseEntity<List<InvoiceNumberDTO>> getAllInvoiceNumbers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InvoiceNumbers");
        Page<InvoiceNumberDTO> page = eventInvoiceNumberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /invoice-numbers/next/{eventId}: get the next invoice number for event.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceNumbers in body
     */
    @GetMapping("/invoice-numbers/next/{eventId}")
    public Integer getNextAvailableInvoiceNumber(@PathVariable Integer eventId) {
        log.debug("REST request to get next available invoice number for event {}", eventId);
        return invoiceGeneratorRepository.getNextInvoiceNumber(eventId);
    }
}
