package com.devoxx.alfioadapter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceHistory.class);
        InvoiceHistory invoiceHistory1 = new InvoiceHistory();
        invoiceHistory1.setId(1L);
        InvoiceHistory invoiceHistory2 = new InvoiceHistory();
        invoiceHistory2.setId(invoiceHistory1.getId());
        assertThat(invoiceHistory1).isEqualTo(invoiceHistory2);
        invoiceHistory2.setId(2L);
        assertThat(invoiceHistory1).isNotEqualTo(invoiceHistory2);
        invoiceHistory1.setId(null);
        assertThat(invoiceHistory1).isNotEqualTo(invoiceHistory2);
    }
}
