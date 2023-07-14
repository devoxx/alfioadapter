package com.devoxx.alfioadapter.web.rest;

import com.devoxx.alfioadapter.domain.InvoiceNumber;
import com.devoxx.alfioadapter.service.InvoiceHistoryService;
import com.devoxx.alfioadapter.service.InvoiceNumberService;
import com.devoxx.alfioadapter.service.RecyclableInvoiceNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing InvoiceNumber.
 */
@RestController
@RequestMapping("/api/invoice")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private final InvoiceNumberService invoiceNumberService;

    private final RecyclableInvoiceNumberService recyclableInvoiceNumberService;

    private final InvoiceHistoryService invoiceHistoryService;

    InvoiceResource(final InvoiceNumberService invoiceNumberService,
                    final RecyclableInvoiceNumberService recyclableInvoiceNumberService,
                    final InvoiceHistoryService invoiceHistoryService) {
        this.invoiceNumberService = invoiceNumberService;
        this.recyclableInvoiceNumberService = recyclableInvoiceNumberService;
        this.invoiceHistoryService = invoiceHistoryService;
    }

    /**
     * POST /api/invoice/{eventId}
     *
     * INVOICE_GENERATION
     *
     * @param eventId  the event short name
     * @param body
     * @return the invoice number
     *
     * @link https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md
     */
    @PostMapping("/{eventId}")
    public Integer getInvoiceNumber(@PathVariable String eventId, @RequestBody String body) {
        log.debug("ALF.IO: getInvoiceNumber for event '{}' with body '{}'", eventId, body);

        if (body.toLowerCase().contains("invoice_generation")) {

            final Integer invoiceNumber = invoiceNumberService.nextInvoiceNumber(eventId);

            invoiceHistoryService.save(eventId, invoiceNumber, InvoiceHistoryService.Action.GENERATE_INVOICE_NUMBER);

            return invoiceNumber;
        } else {

            invoiceHistoryService.save(eventId, -1, InvoiceHistoryService.Action.GET_INVOICE_NUMBER_WRONG_ALF_IO_EVENT);

            log.error("Wrong event name, expecting 'invoice_generation' but was '{}'", body);
        }
        return 0;
    }

    /**
     * Confirm an invoice.
     *
     * RESERVATION_CONFIRMED (additional global variables:  reservation: TicketReservation)
     *
     *
     * {"id":"b1903ef5-bfd8-4d70-8dda-0603675b9e5e",
     *  "validity":"Jun 5, 2018 6:00:00 PM",
     *  "status":"COMPLETE",
     *  "fullName":"Stephan Janssen",
     *  "email":"sja@devoxx.com",
     *  "billingAddress":"test",
     *  "confirmationTimestamp":{"dateTime":{"date":{"year":2018,"month":4,"day":24},"time":{"hour":16,"minute":1,"second":23,"nano":304000000}},
     *  "offset":{"totalSeconds":0},"zone":{"id":"UTC"}},
     *  "paymentMethod":"OFFLINE",
     *  "reminderSent":false,
     *  "automatic":false,
     *  "userLanguage":"en",
     *  "directAssignmentRequested":false,
     *  "invoiceNumber":"13",
     *  "invoiceModel":"{
     *          \"originalTotalPrice\":{\"priceWithVAT\":500,
     *                                  \"discount\":0,
     *                                  \"discountAppliedCount\":0,
     *                                  \"vat\":37},
     *          \"summary\":[{\"name\":\"Unbounded\",
     *                        \"price\":\"4.63\",
     *                        \"priceBeforeVat\":\"4.63\",
     *                        \"amount\":1,
     *                        \"subTotal\":\"4.63\",
     *                        \"subTotalBeforeVat\":\"4.63\",
     *                        \"originalSubTotal\":463,
     *                        \"type\":\"TICKET\"}],
     *          \"free\":false,
     *          \"totalPrice\":\"5.00\",
     *          \"totalVAT\":\"0.37\",
     *          \"waitingForPayment\":false,
     *          \"cashPayment\":false,
     *          \"vatPercentage\":\"424242\",
     *          \"vatStatus\":null,
     *          \"refundedAmount\":null,
     *          \"ticketAmount\":1,
     *          \"vatExempt\":false,
     *          \"notYetPaid\":false,
     *          \"singleTicketOrder\":true,
     *          \"displayVat\":true}",
     *  "invoiceRequested":true,
     *  "usedVatPercent":8.00,
     *  "vatIncluded":false}]
     *
     * @param eventId  the event short name
     * @param body
     * @link https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md
     */
    @PostMapping("/confirmed/{eventId}")
    public void confirmedInvoice(@PathVariable String eventId, @RequestBody String body) {
        log.debug("ALF.IO: confirmedInvoice for event '{}' with body '{}'", eventId, body);

        // TODO
        log.debug("TODO Confirm paid invoice to Exact Online?");

        invoiceHistoryService.save(eventId, -1, InvoiceHistoryService.Action.CONFIRM_INVOICE);
    }

    /**
     * Cancel an invoice.
     *
     * RESERVATION_CANCELLED  (reservationIds: String[] - the reservation IDs)
     *
     * @param eventId  the event short name
     * @param invoiceNumber  the invoice number to recycle
     * @param body
     * @link https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md
     */
    @PostMapping("/cancel/{eventId}/{invoiceNumber}")
    public boolean cancelInvoice(@PathVariable String eventId, @PathVariable Integer invoiceNumber, @RequestBody String body) {
        log.debug("ALF.IO: cancelInvoice for event '{}' with invoice number '{}'", eventId, invoiceNumber);

        if (body.toLowerCase().contains("reservation_cancelled")) {

            // TODO I'm missing the invoice number from ALF.IO that is required to make the invoice number again available into recyclable Invoices.
            final InvoiceNumber foundInvoiceNumber = invoiceNumberService.findByInvoiceNumber(invoiceNumber);

            if (foundInvoiceNumber != null) {
                invoiceHistoryService.save(eventId, invoiceNumber, InvoiceHistoryService.Action.CANCEL_INVOICE);

                recyclableInvoiceNumberService.save(eventId, invoiceNumber);

                invoiceNumberService.delete(foundInvoiceNumber.getId());

                return true;
            } else {

                invoiceHistoryService.save(eventId, invoiceNumber, InvoiceHistoryService.Action.CANCEL_INVOICE_NOT_FOUND);

                log.error("Could not find invoice number '{}'", invoiceNumber);

                return false;
            }

        } else {
            invoiceHistoryService.save(eventId, invoiceNumber, InvoiceHistoryService.Action.CANCEL_INVOICE_WRONG_ALF_IO_EVENT);

            log.error("Wrong event name, expecting 'reservation_cancelled' but was '{}'", body);

            return false;
        }
    }
}
