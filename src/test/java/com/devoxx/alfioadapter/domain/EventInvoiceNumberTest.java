package com.devoxx.alfioadapter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventInvoiceNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventInvoiceNumber.class);
        EventInvoiceNumber eventInvoiceNumber1 = new EventInvoiceNumber();
        eventInvoiceNumber1.setEventId(1);

        EventInvoiceNumber eventInvoiceNumber2 = new EventInvoiceNumber();
        eventInvoiceNumber2.setEventId(1);

        assertThat(eventInvoiceNumber1).isEqualTo(eventInvoiceNumber2);
        eventInvoiceNumber2.setEventId(1);

        assertThat(eventInvoiceNumber1).isNotEqualTo(eventInvoiceNumber2);
        eventInvoiceNumber2.setEventId(1);

        assertThat(eventInvoiceNumber1).isNotEqualTo(eventInvoiceNumber2);
    }
}
