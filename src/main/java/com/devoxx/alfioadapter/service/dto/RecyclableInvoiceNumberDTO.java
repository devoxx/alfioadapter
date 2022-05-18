package com.devoxx.alfioadapter.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.devoxx.alfioadapter.domain.RecyclableInvoiceNumber} entity.
 */
public class RecyclableInvoiceNumberDTO implements Serializable {

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
        if (!(o instanceof RecyclableInvoiceNumberDTO)) {
            return false;
        }

        RecyclableInvoiceNumberDTO recyclableInvoiceNumberDTO = (RecyclableInvoiceNumberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recyclableInvoiceNumberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecyclableInvoiceNumberDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", eventId='" + getEventId() + "'" +
            "}";
    }
}
