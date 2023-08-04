package com.devoxx.alfioadapter.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devoxx.alfioadapter.IntegrationTest;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.devoxx.alfioadapter.repository.InvoiceGeneratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventInvoiceNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventInvoiceNumberResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final Long DEFAULT_INVOICE_NUMBER = 1L;
    private static final Long DEFAULT_EVENT_ID = 2L;
    private static final String ENTITY_API_URL = "/api/invoice-numbers";

    @Autowired
    private MockMvc restInvoiceNumberMockMvc;

    @Test
    @Transactional
    void getAllInvoiceNumbers() throws Exception {

        // Get all the invoiceNumberList
        restInvoiceNumberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)));
    }
}
