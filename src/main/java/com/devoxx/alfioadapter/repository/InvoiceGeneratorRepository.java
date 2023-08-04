package com.devoxx.alfioadapter.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class InvoiceGeneratorRepository {
    private final Logger log = LoggerFactory.getLogger(InvoiceGeneratorRepository.class);
    public static final String ERROR_FETCHING_NEXT_INVOICE_NUMBER = "Error fetching next invoice number";
    public static final String EVENT_INVOICE_NUMBER = "event_invoice_number";
    private static final Integer ZERO_INVOICE_NUMBER = 0;
    private static final String NEXT_INVOICE_NUMBER_QUERY =
        "SELECT nextval('invoice_number_seq')";
    private static final String INSERT_INVOICE_QUERY =
        "INSERT INTO event_invoice_number(event_id, event_invoice_number) VALUES(?, ?)";
    private static final String INSERT_RECYCLED_INVOICE_QUERY =
        "INSERT INTO recycled_invoice_numbers(event_id, event_invoice_number) VALUES(?, ?)";
    private static final String RECYCLED_INVOICE_NUMBER_QUERY =
        "SELECT event_invoice_number FROM recycled_invoice_numbers WHERE event_id = ? ORDER BY event_invoice_number LIMIT 1";
    private static final String DELETE_RECYCLED_NUMBER_QUERY =
        "DELETE FROM recycled_invoice_numbers WHERE event_id = ? AND event_invoice_number = ?";

    private final DataSource dataSource;

    public InvoiceGeneratorRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Get the next invoice number for the given event
     * @param eventId the event id
     * @return the next invoice number
     */
    public Integer getNextInvoiceNumber(Integer eventId) {
        log.debug("Request to get next invoice number for event {}", eventId);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            return getInvoiceNumberFromDatabase(connection, eventId);
        } catch (SQLException e) {
            log.error(ERROR_FETCHING_NEXT_INVOICE_NUMBER, e);
            return ZERO_INVOICE_NUMBER;
        }
    }

    /**
     * get invoice number from database
     * @param connection the connection
     * @param eventId    the event id
     * @return the invoice number
     * @throws SQLException if an SQL error occurs
     */
    private Integer getInvoiceNumberFromDatabase(Connection connection, Integer eventId) throws SQLException {
        try {
            return getRecycledNumbers(eventId, connection)
                .orElseGet(() -> getInvoiceNumber(eventId, connection)
                    .orElseThrow(() -> new InvoiceNumberException(ERROR_FETCHING_NEXT_INVOICE_NUMBER)));
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.commit();
        }
    }


    /**
     * Get the invoice number from the database
     *
     * @param eventId    the event id
     * @param connection the connection
     * @return the invoice number
     */
    private Optional<Integer> getInvoiceNumber(Integer eventId, Connection connection) {
        log.debug("Request to get next invoice number for event {}", eventId);
        try (Statement seqStatement = connection.createStatement();
             ResultSet rsSeq = seqStatement.executeQuery(NEXT_INVOICE_NUMBER_QUERY)) {
                 if (rsSeq.next()) {
                    int invoiceNumber = rsSeq.getInt(1);    // Get invoice number for sequence

                    try (PreparedStatement psInsert = connection.prepareStatement(INSERT_INVOICE_QUERY)) {
                        psInsert.setInt(1, eventId);
                        psInsert.setInt(2, invoiceNumber);
                        psInsert.executeUpdate();
                    }
                    return Optional.of(invoiceNumber);
            }
        } catch (SQLException e) {
            log.error(ERROR_FETCHING_NEXT_INVOICE_NUMBER, e);
            return Optional.of(ZERO_INVOICE_NUMBER);
        }

        return Optional.empty();
    }

    /**
     * Get recycled invoice number for the given event
     *
     * @param eventId    the event id
     * @param connection the connection
     * @return the recycled invoice number
     */
    private Optional<Integer> getRecycledNumbers(Integer eventId, Connection connection) throws SQLException {
        log.debug("Request to get recycled invoice number for event {}", eventId);

        try (PreparedStatement psRecycled = connection.prepareStatement(RECYCLED_INVOICE_NUMBER_QUERY)) {
            psRecycled.setLong(1, eventId);
            try (ResultSet rsRecycled = psRecycled.executeQuery()) {
                if (rsRecycled.next()) {
                    int recycledInvoiceNumber = rsRecycled.getInt(EVENT_INVOICE_NUMBER);

                    try (PreparedStatement psDelete = connection.prepareStatement(DELETE_RECYCLED_NUMBER_QUERY)) {
                        psDelete.setInt(1, eventId);
                        psDelete.setInt(2, recycledInvoiceNumber);
                        psDelete.executeUpdate();
                    }

                    return Optional.of(recycledInvoiceNumber);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Recycle the given invoice number for the given event
     * @param eventId       the event id
     * @param invoiceNumber the invoice number
     * @return true if the invoice number was recycled, false otherwise
     */
    public boolean recycleInvoiceNumber(Integer eventId, Integer invoiceNumber) {
        log.debug("Request to recycle invoice number {} for event {}", invoiceNumber, eventId);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement psRecycled = connection.prepareStatement(INSERT_RECYCLED_INVOICE_QUERY)) {
                    psRecycled.setInt(1, eventId);
                    psRecycled.setInt(2, invoiceNumber);
                    psRecycled.executeUpdate();
                }
            } finally {
                connection.commit();
            }
            return true;
        } catch (SQLException e) {
            log.error("Error recycling invoice number {} for event {}", invoiceNumber, eventId, e);
            return false;
        }
    }

}

