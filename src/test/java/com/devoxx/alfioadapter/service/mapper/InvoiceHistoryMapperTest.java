package com.devoxx.alfioadapter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceHistoryMapperTest {

    private InvoiceHistoryMapper invoiceHistoryMapper;

    @BeforeEach
    public void setUp() {
        invoiceHistoryMapper = new InvoiceHistoryMapperImpl();
    }
}
