package com.devoxx.alfioadapter.web.rest;

import static com.devoxx.alfioadapter.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devoxx.alfioadapter.IntegrationTest;
import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecyclableInvoiceNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecyclableInvoiceNumberResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final Integer DEFAULT_INVOICE_NUMBER = 1;
    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String ENTITY_API_URL = "/api/recyclable-invoice-numbers";

    @Autowired
    private RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository;

    @Autowired
    private MockMvc restRecyclableInvoiceNumberMockMvc;

    private RecyclableInvoiceNumber recyclableInvoiceNumber;

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecyclableInvoiceNumber createEntity() {
        return new RecyclableInvoiceNumber()
            .creationDate(DEFAULT_CREATION_DATE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .eventId(DEFAULT_EVENT_ID);
    }

    @BeforeEach
    public void initTest() {
        recyclableInvoiceNumber = createEntity();
    }

    @Test
    @Transactional
    void getAllRecyclableInvoiceNumbers() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        // Get all the recyclableInvoiceNumberList
        restRecyclableInvoiceNumberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recyclableInvoiceNumber.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)));
    }
}
