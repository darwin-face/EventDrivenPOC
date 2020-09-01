package com.adrian.spendgrid.domain;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SpendgridEmailSendException extends ResponseStatusException {
    public SpendgridEmailSendException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public SpendgridEmailSendException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
