package com.devoxx.alfioadapter.service.mapper;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.service.dto.InvoiceNumberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceNumber} and its DTO {@link InvoiceNumberDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceNumberMapper extends EntityMapper<InvoiceNumberDTO, InvoiceNumber> {}
