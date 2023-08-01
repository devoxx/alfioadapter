package com.devoxx.alfioadapter.repository;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Spring Data JPA repository for the InvoiceNumber entity.
 */
@Repository
public interface InvoiceNumberRepository extends JpaRepository<InvoiceNumber, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InvoiceNumber i where i.eventId = :eventId")
    Optional<InvoiceNumber> findByIdForUpdate(@Param("eventId") String eventId);
}
