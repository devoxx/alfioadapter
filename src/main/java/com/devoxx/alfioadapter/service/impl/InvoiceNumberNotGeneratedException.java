package com.devoxx.alfioadapter.service.impl;

public class InvoiceNumberNotGeneratedException extends RuntimeException {
    public InvoiceNumberNotGeneratedException() {
        super("Unable to generate invoice number.");
    }

    public InvoiceNumberNotGeneratedException(Throwable cause) {
        super("Unable to generate invoice number.", cause);
    }

    // Additional constructors or methods if needed...
}
