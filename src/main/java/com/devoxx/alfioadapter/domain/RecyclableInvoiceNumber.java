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
@Table(name = "recyclable_invoice_number")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecyclableInvoiceNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "invoice_number")
    private Integer invoiceNumber;

    @Column(name = "event_id")
    private String eventId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecyclableInvoiceNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public RecyclableInvoiceNumber creationDate(ZonedDateTime creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public RecyclableInvoiceNumber invoiceNumber(Integer invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getEventId() {
        return this.eventId;
    }

    public RecyclableInvoiceNumber eventId(String eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecyclableInvoiceNumber)) {
            return false;
        }
        return id != null && id.equals(((RecyclableInvoiceNumber) o).id);
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
            ", invoiceNumber=" + getInvoiceNumber() +
            ", eventId='" + getEventId() + "'" +
            "}";
    }
}
