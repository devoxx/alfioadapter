package com.devoxx.alfioadapter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceHistoryDTO.class);
        InvoiceHistoryDTO invoiceHistoryDTO1 = new InvoiceHistoryDTO();
        invoiceHistoryDTO1.setId(1L);
        InvoiceHistoryDTO invoiceHistoryDTO2 = new InvoiceHistoryDTO();
        assertThat(invoiceHistoryDTO1).isNotEqualTo(invoiceHistoryDTO2);
        invoiceHistoryDTO2.setId(invoiceHistoryDTO1.getId());
        assertThat(invoiceHistoryDTO1).isEqualTo(invoiceHistoryDTO2);
        invoiceHistoryDTO2.setId(2L);
        assertThat(invoiceHistoryDTO1).isNotEqualTo(invoiceHistoryDTO2);
        invoiceHistoryDTO1.setId(null);
        assertThat(invoiceHistoryDTO1).isNotEqualTo(invoiceHistoryDTO2);
    }
}
