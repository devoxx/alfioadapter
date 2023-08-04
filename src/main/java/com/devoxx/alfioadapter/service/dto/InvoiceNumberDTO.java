package com.devoxx.alfioadapter.service.dto;

import com.devoxx.alfioadapter.domain.EventInvoiceNumber;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link EventInvoiceNumber} entity.
 */
public class InvoiceNumberDTO implements Serializable {

    private Long id;

    private ZonedDateTime creationDate;

    private Integer invoiceNumber;

    private String eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceNumberDTO)) {
            return false;
        }

        InvoiceNumberDTO invoiceNumberDTO = (InvoiceNumberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceNumberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceNumberDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", eventId='" + getEventId() + "'" +
            "}";
    }
}
