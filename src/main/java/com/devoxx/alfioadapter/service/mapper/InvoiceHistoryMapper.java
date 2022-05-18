package com.devoxx.alfioadapter.service.mapper;

import com.devoxx.alfioadapter.domain.InvoiceHistory;
import com.devoxx.alfioadapter.service.dto.InvoiceHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceHistory} and its DTO {@link InvoiceHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceHistoryMapper extends EntityMapper<InvoiceHistoryDTO, InvoiceHistory> {}
