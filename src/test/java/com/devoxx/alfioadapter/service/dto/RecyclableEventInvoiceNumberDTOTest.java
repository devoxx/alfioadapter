package com.devoxx.alfioadapter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoxx.alfioadapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecyclableEventInvoiceNumberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecyclableInvoiceNumberDTO.class);
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO1 = new RecyclableInvoiceNumberDTO();
        recyclableInvoiceNumberDTO1.setId(1L);
        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO2 = new RecyclableInvoiceNumberDTO();
        assertThat(recyclableInvoiceNumberDTO1).isNotEqualTo(recyclableInvoiceNumberDTO2);
        recyclableInvoiceNumberDTO2.setId(recyclableInvoiceNumberDTO1.getId());
        assertThat(recyclableInvoiceNumberDTO1).isEqualTo(recyclableInvoiceNumberDTO2);
        recyclableInvoiceNumberDTO2.setId(2L);
        assertThat(recyclableInvoiceNumberDTO1).isNotEqualTo(recyclableInvoiceNumberDTO2);
        recyclableInvoiceNumberDTO1.setId(null);
        assertThat(recyclableInvoiceNumberDTO1).isNotEqualTo(recyclableInvoiceNumberDTO2);
    }
}
