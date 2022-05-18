package com.devoxx.alfioadapter.web.rest;

import static com.devoxx.alfioadapter.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devoxx.alfioadapter.IntegrationTest;
import com.devoxx.alfioadapter.domain.InvoiceHistory;
import com.devoxx.alfioadapter.repository.InvoiceHistoryRepository;
import com.devoxx.alfioadapter.service.dto.InvoiceHistoryDTO;
import com.devoxx.alfioadapter.service.mapper.InvoiceHistoryMapper;
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
 * Integration tests for the {@link InvoiceHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceHistoryResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_INVOICE_NUMBER = 1;
    private static final Integer UPDATED_INVOICE_NUMBER = 2;

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceHistoryRepository invoiceHistoryRepository;

    @Autowired
    private InvoiceHistoryMapper invoiceHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceHistoryMockMvc;

    private InvoiceHistory invoiceHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceHistory createEntity(EntityManager em) {
        InvoiceHistory invoiceHistory = new InvoiceHistory()
            .creationDate(DEFAULT_CREATION_DATE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .eventId(DEFAULT_EVENT_ID)
            .status(DEFAULT_STATUS);
        return invoiceHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceHistory createUpdatedEntity(EntityManager em) {
        InvoiceHistory invoiceHistory = new InvoiceHistory()
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID)
            .status(UPDATED_STATUS);
        return invoiceHistory;
    }

    @BeforeEach
    public void initTest() {
        invoiceHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceHistory() throws Exception {
        int databaseSizeBeforeCreate = invoiceHistoryRepository.findAll().size();
        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);
        restInvoiceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceHistory testInvoiceHistory = invoiceHistoryList.get(invoiceHistoryList.size() - 1);
        assertThat(testInvoiceHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testInvoiceHistory.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoiceHistory.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
        assertThat(testInvoiceHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createInvoiceHistoryWithExistingId() throws Exception {
        // Create the InvoiceHistory with an existing ID
        invoiceHistory.setId(1L);
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        int databaseSizeBeforeCreate = invoiceHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceHistories() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        // Get all the invoiceHistoryList
        restInvoiceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getInvoiceHistory() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        // Get the invoiceHistory
        restInvoiceHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceHistory.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceHistory() throws Exception {
        // Get the invoiceHistory
        restInvoiceHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoiceHistory() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();

        // Update the invoiceHistory
        InvoiceHistory updatedInvoiceHistory = invoiceHistoryRepository.findById(invoiceHistory.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceHistory are not directly saved in db
        em.detach(updatedInvoiceHistory);
        updatedInvoiceHistory
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID)
            .status(UPDATED_STATUS);
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(updatedInvoiceHistory);

        restInvoiceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
        InvoiceHistory testInvoiceHistory = invoiceHistoryList.get(invoiceHistoryList.size() - 1);
        assertThat(testInvoiceHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testInvoiceHistory.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceHistory.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testInvoiceHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceHistoryWithPatch() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();

        // Update the invoiceHistory using partial update
        InvoiceHistory partialUpdatedInvoiceHistory = new InvoiceHistory();
        partialUpdatedInvoiceHistory.setId(invoiceHistory.getId());

        partialUpdatedInvoiceHistory.eventId(UPDATED_EVENT_ID);

        restInvoiceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceHistory))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
        InvoiceHistory testInvoiceHistory = invoiceHistoryList.get(invoiceHistoryList.size() - 1);
        assertThat(testInvoiceHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testInvoiceHistory.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoiceHistory.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testInvoiceHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceHistoryWithPatch() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();

        // Update the invoiceHistory using partial update
        InvoiceHistory partialUpdatedInvoiceHistory = new InvoiceHistory();
        partialUpdatedInvoiceHistory.setId(invoiceHistory.getId());

        partialUpdatedInvoiceHistory
            .creationDate(UPDATED_CREATION_DATE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .eventId(UPDATED_EVENT_ID)
            .status(UPDATED_STATUS);

        restInvoiceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceHistory))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
        InvoiceHistory testInvoiceHistory = invoiceHistoryList.get(invoiceHistoryList.size() - 1);
        assertThat(testInvoiceHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testInvoiceHistory.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceHistory.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testInvoiceHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceHistory() throws Exception {
        int databaseSizeBeforeUpdate = invoiceHistoryRepository.findAll().size();
        invoiceHistory.setId(count.incrementAndGet());

        // Create the InvoiceHistory
        InvoiceHistoryDTO invoiceHistoryDTO = invoiceHistoryMapper.toDto(invoiceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceHistory in the database
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceHistory() throws Exception {
        // Initialize the database
        invoiceHistoryRepository.saveAndFlush(invoiceHistory);

        int databaseSizeBeforeDelete = invoiceHistoryRepository.findAll().size();

        // Delete the invoiceHistory
        restInvoiceHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceHistory> invoiceHistoryList = invoiceHistoryRepository.findAll();
        assertThat(invoiceHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
