package com.devoxx.alfioadapter.web.rest;

import static com.devoxx.alfioadapter.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devoxx.alfioadapter.IntegrationTest;
import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.repository.RecyclableInvoiceNumberRepository;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
import com.devoxx.alfioadapter.service.mapper.RecyclableInvoiceNumberMapper;
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
 * Integration tests for the {@link RecyclableInvoiceNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecyclableInvoiceNumberResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_INVOICE_NUMBER = 1;
    private static final Integer UPDATED_INVOICE_NUMBER = 2;

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recyclable-invoice-numbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecyclableInvoiceNumberRepository recyclableInvoiceNumberRepository;

    @Autowired
    private RecyclableInvoiceNumberMapper recyclableInvoiceNumberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecyclableInvoiceNumberMockMvc;

    private RecyclableInvoiceNumber recyclableInvoiceNumber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecyclableInvoiceNumber createEntity(EntityManager em) {
        RecyclableInvoiceNumber recyclableInvoiceNumber = new RecyclableInvoiceNumber()
            .creationDate(DEFAULT_CREATION_DATE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .eventId(DEFAULT_EVENT_ID);
        return recyclableInvoiceNumber;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecyclableInvoiceNumber createUpdatedEntity(EntityManager em) {
        RecyclableInvoiceNumber recyclableInvoiceNumber = new RecyclableInvoiceNumber()
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID);
        return recyclableInvoiceNumber;
    }

    @BeforeEach
    public void initTest() {
        recyclableInvoiceNumber = createEntity(em);
    }

    @Test
    @Transactional
    void createRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeCreate = recyclableInvoiceNumberRepository.findAll().size();
        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);
        restRecyclableInvoiceNumberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeCreate + 1);
        RecyclableInvoiceNumber testRecyclableInvoiceNumber = recyclableInvoiceNumberList.get(recyclableInvoiceNumberList.size() - 1);
        assertThat(testRecyclableInvoiceNumber.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRecyclableInvoiceNumber.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testRecyclableInvoiceNumber.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
    }

    @Test
    @Transactional
    void createRecyclableInvoiceNumberWithExistingId() throws Exception {
        // Create the RecyclableInvoiceNumber with an existing ID
        recyclableInvoiceNumber.setId(1L);
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        int databaseSizeBeforeCreate = recyclableInvoiceNumberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecyclableInvoiceNumberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeCreate);
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

    @Test
    @Transactional
    void getRecyclableInvoiceNumber() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        // Get the recyclableInvoiceNumber
        restRecyclableInvoiceNumberMockMvc
            .perform(get(ENTITY_API_URL_ID, recyclableInvoiceNumber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recyclableInvoiceNumber.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID));
    }

    @Test
    @Transactional
    void getNonExistingRecyclableInvoiceNumber() throws Exception {
        // Get the recyclableInvoiceNumber
        restRecyclableInvoiceNumberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecyclableInvoiceNumber() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();

        // Update the recyclableInvoiceNumber
        RecyclableInvoiceNumber updatedRecyclableInvoiceNumber = recyclableInvoiceNumberRepository
            .findById(recyclableInvoiceNumber.getId())
            .get();
        // Disconnect from session so that the updates on updatedRecyclableInvoiceNumber are not directly saved in db
        em.detach(updatedRecyclableInvoiceNumber);
        updatedRecyclableInvoiceNumber.creationDate(UPDATED_CREATION_DATE).invoiceNumber(UPDATED_INVOICE_NUMBER).eventId(UPDATED_EVENT_ID);
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(updatedRecyclableInvoiceNumber);

        restRecyclableInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recyclableInvoiceNumberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        RecyclableInvoiceNumber testRecyclableInvoiceNumber = recyclableInvoiceNumberList.get(recyclableInvoiceNumberList.size() - 1);
        assertThat(testRecyclableInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRecyclableInvoiceNumber.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testRecyclableInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void putNonExistingRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recyclableInvoiceNumberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecyclableInvoiceNumberWithPatch() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();

        // Update the recyclableInvoiceNumber using partial update
        RecyclableInvoiceNumber partialUpdatedRecyclableInvoiceNumber = new RecyclableInvoiceNumber();
        partialUpdatedRecyclableInvoiceNumber.setId(recyclableInvoiceNumber.getId());

        partialUpdatedRecyclableInvoiceNumber.creationDate(UPDATED_CREATION_DATE).eventId(UPDATED_EVENT_ID);

        restRecyclableInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecyclableInvoiceNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecyclableInvoiceNumber))
            )
            .andExpect(status().isOk());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        RecyclableInvoiceNumber testRecyclableInvoiceNumber = recyclableInvoiceNumberList.get(recyclableInvoiceNumberList.size() - 1);
        assertThat(testRecyclableInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRecyclableInvoiceNumber.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testRecyclableInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void fullUpdateRecyclableInvoiceNumberWithPatch() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();

        // Update the recyclableInvoiceNumber using partial update
        RecyclableInvoiceNumber partialUpdatedRecyclableInvoiceNumber = new RecyclableInvoiceNumber();
        partialUpdatedRecyclableInvoiceNumber.setId(recyclableInvoiceNumber.getId());

        partialUpdatedRecyclableInvoiceNumber
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID);

        restRecyclableInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecyclableInvoiceNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecyclableInvoiceNumber))
            )
            .andExpect(status().isOk());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
        RecyclableInvoiceNumber testRecyclableInvoiceNumber = recyclableInvoiceNumberList.get(recyclableInvoiceNumberList.size() - 1);
        assertThat(testRecyclableInvoiceNumber.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRecyclableInvoiceNumber.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testRecyclableInvoiceNumber.getEventId()).isEqualTo(UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recyclableInvoiceNumberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecyclableInvoiceNumber() throws Exception {
        int databaseSizeBeforeUpdate = recyclableInvoiceNumberRepository.findAll().size();
        recyclableInvoiceNumber.setId(count.incrementAndGet());

        // Create the RecyclableInvoiceNumber
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = recyclableInvoiceNumberMapper.toDto(recyclableInvoiceNumber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecyclableInvoiceNumberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recyclableInvoiceNumberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecyclableInvoiceNumber in the database
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecyclableInvoiceNumber() throws Exception {
        // Initialize the database
        recyclableInvoiceNumberRepository.saveAndFlush(recyclableInvoiceNumber);

        int databaseSizeBeforeDelete = recyclableInvoiceNumberRepository.findAll().size();

        // Delete the recyclableInvoiceNumber
        restRecyclableInvoiceNumberMockMvc
            .perform(delete(ENTITY_API_URL_ID, recyclableInvoiceNumber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecyclableInvoiceNumber> recyclableInvoiceNumberList = recyclableInvoiceNumberRepository.findAll();
        assertThat(recyclableInvoiceNumberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
