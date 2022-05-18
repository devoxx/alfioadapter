package com.devoxx.alfioadapter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceNumber.class);
        InvoiceNumber invoiceNumber1 = new InvoiceNumber();
        invoiceNumber1.setId(1L);
        InvoiceNumber invoiceNumber2 = new InvoiceNumber();
        invoiceNumber2.setId(invoiceNumber1.getId());
        assertThat(invoiceNumber1).isEqualTo(invoiceNumber2);
        invoiceNumber2.setId(2L);
        assertThat(invoiceNumber1).isNotEqualTo(invoiceNumber2);
        invoiceNumber1.setId(null);
        assertThat(invoiceNumber1).isNotEqualTo(invoiceNumber2);
    }
}
