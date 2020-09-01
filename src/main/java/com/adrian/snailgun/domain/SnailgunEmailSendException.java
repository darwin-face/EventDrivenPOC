package com.adrian.snailgun.domain;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SnailgunEmailSendException extends ResponseStatusException {
    public SnailgunEmailSendException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public SnailgunEmailSendException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
