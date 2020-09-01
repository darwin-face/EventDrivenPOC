package com.adrian.email.api;

import com.adrian.email.domain.EmailService;
import com.adrian.email.domain.EmailValidationUtils;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(path = {"/email/", "/email"})
    public ResponseEntity sendEmail(@RequestBody EmailRequest emailRequest) {
        // validate email, then send to the external API
        final Validation<Seq<String>, EmailCreateResponse> emailValidation = EmailValidationUtils.validateEmail(emailRequest)
                .map(validEmail -> emailService.createAndSendEmail(validEmail));

        if(emailValidation.isValid()){
            return new ResponseEntity<>(emailValidation.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(emailValidation.getError(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = {"/updateemail/{emailId}"})
    public ResponseEntity updateSnailgunEmail(@PathVariable String emailId) {
        final Validation<String, Try<String>> snailgunUpdateValidation = EmailValidationUtils.validateSnailgunID(emailId)
                .map(validId -> emailService.updateSnailgunEmail(validId));

        if(snailgunUpdateValidation.isValid()){
            return new ResponseEntity<>(snailgunUpdateValidation.get().get(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(snailgunUpdateValidation.getError(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = {"/greeting/", "/greeting"})
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("The send email application is running!");
    }
}