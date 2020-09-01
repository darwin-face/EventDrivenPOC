package com.adrian.email.api;

import lombok.Value;

import java.util.UUID;

@Value
public class EmailCreateResponse {
    UUID id;
    String emailServiceName;
}
