package com.devoxx.alfioadapter.web.rest.errors;

public class MaxInvoiceNumberReachedException extends RuntimeException {

        public MaxInvoiceNumberReachedException() {
            super("Maximum invoice number reached");
        }
}
