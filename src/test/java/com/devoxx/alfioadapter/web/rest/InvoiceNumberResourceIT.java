package com.devoxx.alfioadapter.web.rest;

import static com.devoxx.alfioadapter.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devoxx.alfioadapter.IntegrationTest;
import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.repository.InvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceNumberMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvoiceNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceNumberResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_INVOICE_NUMBER = 1;
    private static final Integer UPDATED_INVOICE_NUMBER = 2;

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-numbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceNumberRepository invoiceNumberRepository;

    @Autowired
    private InvoiceNumberMapper invoiceNumberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceNumberMockMvc;

    private InvoiceNumber invoiceNumber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceNumber createEntity(EntityManager em) {
        InvoiceNumber invoiceNumber = new InvoiceNumber()
            .creationDate(DEFAULT_CREATION_DATE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .eventId(DEFAULT_EVENT_ID);
        return invoiceNumber;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceNumber createUpdatedEntity(EntityManager em) {
        InvoiceNumber invoiceNumber = new InvoiceNumber()
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID);
        return invoiceNumber;
    }

    @BeforeEach
    public void initTest() {
        invoiceNumber = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceNumber() throws Exception {
        int databaseSizeBeforeCreate = invoiceNumberRepository.findAll().size();
        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);
        restInvoiceNumberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceNumber testInvoiceNumber = invoiceNumberList.get(invoiceNumberList.size() - 1);
        assertThat(testInvoiceNumber.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testInvoiceNumber.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoiceNumber.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
    }

    @Test
    @Transactional
    void createInvoiceNumberWithExistingId() throws Exception {
        // Create the InvoiceNumber with an existing ID
        invoiceNumber.setId(1L);
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        int databaseSizeBeforeCreate = invoiceNumberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceNumberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceNumbers() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        // Get all the invoiceNumberList
        restInvoiceNumberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceNumber.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)));
    }

    @Test
    @Transactional
    void getInvoiceNumber() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        // Get the invoiceNumber
        restInvoiceNumberMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceNumber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceNumber.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceNumber() throws Exception {
        // Get the invoiceNumber
        restInvoiceNumberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoiceNumber() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();

        // Update the invoiceNumber
        InvoiceNumber updatedInvoiceNumber = invoiceNumberRepository.findById(invoiceNumber.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceNumber are not directly saved in db
        em.detach(updatedInvoiceNumber);
        updatedInvoiceNumber.creationDate(UPDATED_CREATION_DATE).invoiceNumber(UPDATED_INVOICE_NUMBER).eventId(UPDATED_EVENT_ID);
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(updatedInvoiceNumber);

        restInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceNumberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        InvoiceNumber testInvoiceNumber = invoiceNumberList.get(invoiceNumberList.size() - 1);
        assertThat(testInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testInvoiceNumber.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceNumberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceNumberWithPatch() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();

        // Update the invoiceNumber using partial update
        InvoiceNumber partialUpdatedInvoiceNumber = new InvoiceNumber();
        partialUpdatedInvoiceNumber.setId(invoiceNumber.getId());

        partialUpdatedInvoiceNumber.creationDate(UPDATED_CREATION_DATE).invoiceNumber(UPDATED_INVOICE_NUMBER).eventId(UPDATED_EVENT_ID);

        restInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceNumber))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        InvoiceNumber testInvoiceNumber = invoiceNumberList.get(invoiceNumberList.size() - 1);
        assertThat(testInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testInvoiceNumber.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceNumberWithPatch() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();

        // Update the invoiceNumber using partial update
        InvoiceNumber partialUpdatedInvoiceNumber = new InvoiceNumber();
        partialUpdatedInvoiceNumber.setId(invoiceNumber.getId());

        partialUpdatedInvoiceNumber.creationDate(UPDATED_CREATION_DATE).invoiceNumber(UPDATED_INVOICE_NUMBER).eventId(UPDATED_EVENT_ID);

        restInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceNumber))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        InvoiceNumber testInvoiceNumber = invoiceNumberList.get(invoiceNumberList.size() - 1);
        assertThat(testInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testInvoiceNumber.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceNumberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = invoiceNumberRepository.findAll().size();
        invoiceNumber.setId(count.incrementAndGet());

        // Create the InvoiceNumber
        InvoiceNumberDTO invoiceNumberDTO = invoiceNumberMapper.toDto(invoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceNumberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceNumber in the database
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceNumber() throws Exception {
        // Initialize the database
        invoiceNumberRepository.saveAndFlush(invoiceNumber);

        int databaseSizeBeforeDelete = invoiceNumberRepository.findAll().size();

        // Delete the invoiceNumber
        restInvoiceNumberMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceNumber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceNumber> invoiceNumberList = invoiceNumberRepository.findAll();
        assertThat(invoiceNumberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
