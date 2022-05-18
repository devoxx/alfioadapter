package com.devoxx.alfioadapter.web.rest;

import com.devoxx.alfioadapter.service.RecyclableInvoiceNumberService;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
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
 * REST controller for managing RecyclableInvoiceNumber.
 */
@RestController
@RequestMapping("/api")
public class RecyclableInvoiceNumberResource {

    private final Logger log = LoggerFactory.getLogger(RecyclableInvoiceNumberResource.class);

    private final RecyclableInvoiceNumberService recyclableInvoiceNumberService;

    public RecyclableInvoiceNumberResource(RecyclableInvoiceNumberService recyclableInvoiceNumberService) {
        this.recyclableInvoiceNumberService = recyclableInvoiceNumberService;
    }

    /**
     * GET  /recyclable-invoice-numbers : get all the recyclableInvoiceNumbers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recyclableInvoiceNumbers in body
     */
    @GetMapping("/recyclable-invoice-numbers")
    public ResponseEntity<List<RecyclableInvoiceNumberDTO>> getAllRecyclableInvoiceNumbers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RecyclableInvoiceNumbers");
        Page<RecyclableInvoiceNumberDTO> page = recyclableInvoiceNumberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
