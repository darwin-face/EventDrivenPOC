package com.adrian.snailgun.domain;

import com.adrian.email.domain.ValidEmail;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SnailgunEmail {
    String from_email;
    String from_name;
    String to_email;
    String to_name;
    String subject;
    String body;

    public static SnailgunEmail from(ValidEmail email) {
        return SnailgunEmail.builder()
                .from_email(email.getFromEmail())
                .from_name(email.getFromName())
                .to_email(email.getToEmail())
                .to_name(email.getToName())
                .subject(email.getSubject())
                .body(email.getBody())
                .build();
    }
}

// request body:
// {
//      "from_email": "noreply@mybrightwheel.com",
//      "from_name": "brightwheel",
//      "to_email": "susan@abcpreschool.org",
//      "to_name": "Miss Susan",
//      "subject": "Your Weekly Report",
//      "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>"
//  }