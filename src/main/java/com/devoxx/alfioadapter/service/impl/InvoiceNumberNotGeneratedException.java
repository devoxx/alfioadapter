package com.devoxx.alfioadapter.service.impl;

public class InvoiceNumberNotGeneratedException extends RuntimeException {

    public InvoiceNumberNotGeneratedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
