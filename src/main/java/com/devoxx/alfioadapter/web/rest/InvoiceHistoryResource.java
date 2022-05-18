package com.devoxx.alfioadapter.web.rest;

import com.devoxx.alfioadapter.service.InvoiceHistoryService;
import com.devoxx.alfioadapter.service.dto.InvoiceHistoryDTO;
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
 * REST controller for managing InvoiceHistory.
 */
@RestController
@RequestMapping("/api")
public class InvoiceHistoryResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceHistoryResource.class);

    private final InvoiceHistoryService invoiceHistoryService;

    InvoiceHistoryResource(InvoiceHistoryService invoiceHistoryService) {
        this.invoiceHistoryService = invoiceHistoryService;
    }

    /**
     * GET  /invoice-histories : get all the invoiceHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceHistories in body
     */
    @GetMapping("/invoice-histories")
    public ResponseEntity<List<InvoiceHistoryDTO>> getAllInvoiceHistories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InvoiceHistories");
        Page<InvoiceHistoryDTO> page = invoiceHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
