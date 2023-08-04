package com.devoxx.alfioadapter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecyclableEventInvoiceNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecycledInvoiceNumber.class);
        RecycledInvoiceNumber recyclableInvoiceNumber1 = new RecycledInvoiceNumber();
        recyclableInvoiceNumber1.setId(1L);
        RecycledInvoiceNumber recyclableInvoiceNumber2 = new RecycledInvoiceNumber();
        recyclableInvoiceNumber2.setId(recyclableInvoiceNumber1.getId());
        assertThat(recyclableInvoiceNumber1).isEqualTo(recyclableInvoiceNumber2);
        recyclableInvoiceNumber2.setId(2L);
        assertThat(recyclableInvoiceNumber1).isNotEqualTo(recyclableInvoiceNumber2);
        recyclableInvoiceNumber1.setId(null);
        assertThat(recyclableInvoiceNumber1).isNotEqualTo(recyclableInvoiceNumber2);
    }
}
