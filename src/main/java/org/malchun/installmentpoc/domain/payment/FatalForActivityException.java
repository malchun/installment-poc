package org.malchun.installmentpoc.domain.payment;

public class FatalForActivityException extends RuntimeException {
    public FatalForActivityException(String message) {
        super(message);
    }
}
