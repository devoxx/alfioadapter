package com.devoxx.alfioadapter.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceNumber.
 */
@Entity
@Table(name = "event_invoice_number")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventInvoiceNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "invoice_number")
    private Integer invoiceNumber;

    @Column(name = "event_id")
    private Integer eventId;

    public Long getId() {
        return this.id;
    }

    public EventInvoiceNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public EventInvoiceNumber creationDate(ZonedDateTime creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public EventInvoiceNumber invoiceNumber(Integer invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getEventId() {
        return this.eventId;
    }

    public EventInvoiceNumber eventId(Integer eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventInvoiceNumber)) {
            return false;
        }
        return id != null && id.equals(((EventInvoiceNumber) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceNumber{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", eventId='" + getEventId() + "'" +
            "}";
    }
}
