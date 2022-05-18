package com.devoxx.alfioadapter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecyclableInvoiceNumberMapperTest {

    private RecyclableInvoiceNumberMapper recyclableInvoiceNumberMapper;

    @BeforeEach
    public void setUp() {
        recyclableInvoiceNumberMapper = new RecyclableInvoiceNumberMapperImpl();
    }
}
