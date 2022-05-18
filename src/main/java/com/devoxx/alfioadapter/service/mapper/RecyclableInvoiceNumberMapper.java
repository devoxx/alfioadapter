package com.devoxx.alfioadapter.service.mapper;

import com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber;
import com.devoxx.alfioadapter.service.dto.RecyclableInvoiceNumberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecyclableInvoiceNumber} and its DTO {@link RecyclableInvoiceNumberDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecyclableInvoiceNumberMapper extends EntityMapper<RecyclableInvoiceNumberDTO, RecyclableInvoiceNumber> {}
