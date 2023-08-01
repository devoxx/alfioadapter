package com.devoxx.alfioadapter.service;

import com.devoxx.alfioadapter.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class InvoiceNumberServiceIT {

    @Autowired
    private InvoiceNumberService invoiceNumberService;

    @Autowired
    private
    @Test
    void nextInvoiceNumber() {
        Integer invoiceNumber = invoiceNumberService.nextInvoiceNumber("99");
        assertThat(invoiceNumber).isNotEqualTo(null);
    }

    @Test
    void multipleInvoiceNumbers() {

        Integer invoiceNumber1 = invoiceNumberService.nextInvoiceNumber("99");
        Integer invoiceNumber2 = invoiceNumberService.nextInvoiceNumber("99");
        Integer invoiceNumber3 = invoiceNumberService.nextInvoiceNumber("99");
        Integer invoiceNumber4 = invoiceNumberService.nextInvoiceNumber("99");
        Integer invoiceNumber5 = invoiceNumberService.nextInvoiceNumber("99");

        assertThat(invoiceNumber1).isNotNull();
        assertThat(invoiceNumber2).isNotNull();
        assertThat(invoiceNumber3).isNotNull();
        assertThat(invoiceNumber4).isNotNull();
        assertThat(invoiceNumber5).isNotNull();

        for (boolean b :
            new boolean[] {
                invoiceNumber1 < invoiceNumber2,
                invoiceNumber2 < invoiceNumber3,
                invoiceNumber3 < invoiceNumber4,
                invoiceNumber4 < invoiceNumber5}) {
            assertThat(b).isTrue();
        }
    }
}
