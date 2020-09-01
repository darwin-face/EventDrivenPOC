package com.adrian.email.domain;

import com.adrian.email.api.EmailCreateResponse;
import io.vavr.control.Try;

public interface EmailService {
    EmailCreateResponse createAndSendEmail(ValidEmail validEmail);

    Try<String> updateSnailgunEmail(String snailgunId);
}
