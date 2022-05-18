package com.devoxx.alfioadapter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceNumberMapperTest {

    private InvoiceNumberMapper invoiceNumberMapper;

    @BeforeEach
    public void setUp() {
        invoiceNumberMapper = new InvoiceNumberMapperImpl();
    }
}
