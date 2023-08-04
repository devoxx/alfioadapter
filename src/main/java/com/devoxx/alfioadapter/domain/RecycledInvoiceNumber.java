package com.devoxx.alfioadapter.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecyclableInvoiceNumber.
 */
@Entity
@Table(name = "recycled_invoice_numbers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecycledInvoiceNumber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "event_invoice_number")
    private Integer invoiceNumber;

    @Column(name = "event_id")
    private Integer eventId;

    public Long getId() {
        return this.id;
    }

    public RecycledInvoiceNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public RecycledInvoiceNumber creationDate(ZonedDateTime creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public RecycledInvoiceNumber invoiceNumber(Integer invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getEventId() {
        return this.eventId;
    }

    public RecycledInvoiceNumber eventId(Integer eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecycledInvoiceNumber)) {
            return false;
        }
        return id != null && id.equals(((RecycledInvoiceNumber) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecyclableInvoiceNumber{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", eventInvoiceNumber=" + getInvoiceNumber() +
            ", eventId='" + getEventId() + "'" +
            "}";
    }
}
