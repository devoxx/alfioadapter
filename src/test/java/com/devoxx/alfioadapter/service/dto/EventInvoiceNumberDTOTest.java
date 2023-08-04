package com.devoxx.alfioadapter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventInvoiceNumberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceNumberDTO.class);
        InvoiceNumberDTO invoiceNumberDTO1 = new InvoiceNumberDTO();
        invoiceNumberDTO1.setId(1L);
        InvoiceNumberDTO invoiceNumberDTO2 = new InvoiceNumberDTO();
        assertThat(invoiceNumberDTO1).isNotEqualTo(invoiceNumberDTO2);
        invoiceNumberDTO2.setId(invoiceNumberDTO1.getId());
        assertThat(invoiceNumberDTO1).isEqualTo(invoiceNumberDTO2);
        invoiceNumberDTO2.setId(2L);
        assertThat(invoiceNumberDTO1).isNotEqualTo(invoiceNumberDTO2);
        invoiceNumberDTO1.setId(null);
        assertThat(invoiceNumberDTO1).isNotEqualTo(invoiceNumberDTO2);
    }
}
