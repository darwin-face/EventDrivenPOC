package com.adrian.email.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "An invalid email service was supplied in the application properties.")
public class EmailServiceNotFoundException extends RuntimeException{
}
