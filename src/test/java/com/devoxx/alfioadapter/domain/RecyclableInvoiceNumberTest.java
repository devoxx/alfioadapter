package com.devoxx.alfioadapter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecyclableInvoiceNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecyclableInvoiceNumber.class);
        RecyclableInvoiceNumber recyclableInvoiceNumber1 = new RecyclableInvoiceNumber();
        recyclableInvoiceNumber1.setId(1L);
        RecyclableInvoiceNumber recyclableInvoiceNumber2 = new RecyclableInvoiceNumber();
        recyclableInvoiceNumber2.setId(recyclableInvoiceNumber1.getId());
        assertThat(recyclableInvoiceNumber1).isEqualTo(recyclableInvoiceNumber2);
        recyclableInvoiceNumber2.setId(2L);
        assertThat(recyclableInvoiceNumber1).isNotEqualTo(recyclableInvoiceNumber2);
        recyclableInvoiceNumber1.setId(null);
        assertThat(recyclableInvoiceNumber1).isNotEqualTo(recyclableInvoiceNumber2);
    }
}
