package com.devoxx.alfioadapter.web.rest;

import com.devoxx.alfioadapter.repository.InvoiceGeneratorRepository;
import com.devoxx.alfioadapter.service.InvoiceHistoryService;
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
    public static final String RESERVATION_CANCELLED = "reservation_cancelled";
    public static final String INVOICE_GENERATION = "invoice_generation";
    private static final Integer ZERO_INVOICE_NUMBER = 0;
    private final InvoiceGeneratorRepository invoiceGeneratorRepository;
    private final InvoiceHistoryService invoiceHistoryService;

    InvoiceResource(final InvoiceGeneratorRepository invoiceGeneratorRepository,
                    InvoiceHistoryService invoiceHistoryService) {
        this.invoiceGeneratorRepository = invoiceGeneratorRepository;
        this.invoiceHistoryService = invoiceHistoryService;
    }

    /**
     * POST /api/invoice/{eventId}
     * INVOICE_GENERATION
     * @param eventId  the event short name
     * @param body the body of the request
     * @return the invoice number
     * @link <a href="https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md">More info</a>
     */
    @PostMapping("/{eventId}")
    public Integer getInvoiceNumber(@PathVariable Integer eventId, @RequestBody String body) {
        log.debug("ALF.IO: getInvoiceNumber for event '{}' with body '{}'", eventId, body);

        if (eventId == null) {
            return ZERO_INVOICE_NUMBER;
        }

        if (body.toLowerCase().contains(INVOICE_GENERATION)) {

            return invoiceGeneratorRepository.getNextInvoiceNumber(eventId);

        } else {

            invoiceHistoryService.save(eventId, -1, InvoiceHistoryService.Action.GET_INVOICE_NUMBER_WRONG_ALF_IO_EVENT);

            log.error("Wrong event name, expecting 'invoice_generation' but was '{}'", body);
        }
        return ZERO_INVOICE_NUMBER;
    }


    /**
     * Confirm an invoice.
     * RESERVATION_CONFIRMED (additional global variables:  reservation: TicketReservation)
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
     * @param body the body of the request
     * @link <a href="https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md">more info</a>
     */
    @PostMapping("/confirmed/{eventId}")
    public void confirmedInvoice(@PathVariable Integer eventId, @RequestBody String body) {
        log.debug("ALF.IO: confirmedInvoice for event '{}' with body '{}'", eventId, body);

        invoiceHistoryService.save(eventId, -1, InvoiceHistoryService.Action.CONFIRM_INVOICE);
    }

    /**
     * Cancel an invoice.
     * RESERVATION_CANCELLED  (reservationIds: String[] - the reservation IDs)
     * @param eventId  the event short name
     * @param invoiceNumber  the invoice number to recycle
     * @param body the body of the request
     * @link <a href="https://github.com/alfio-event/alf.io/blob/master/docs/extensions-howto.md">more info</a>
     */
    @PostMapping("/cancel/{eventId}/{invoiceNumber}")
    public boolean cancelInvoice(@PathVariable Integer eventId, @PathVariable Integer invoiceNumber, @RequestBody String body) {
        log.debug("ALF.IO: cancelInvoice for event '{}' with invoice number '{}'", eventId, invoiceNumber);

        if (body.toLowerCase().contains(RESERVATION_CANCELLED)) {

            if (invoiceNumber != null) {
                invoiceHistoryService.save(eventId, invoiceNumber, InvoiceHistoryService.Action.CANCEL_INVOICE);

                return invoiceGeneratorRepository.recycleInvoiceNumber(eventId, invoiceNumber);

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
