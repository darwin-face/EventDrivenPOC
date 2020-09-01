package com.adrian.spendgrid.domain;

import com.adrian.email.domain.ValidEmail;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpendgridEmail {
    String sender;
    String recipient;
    String subject;
    String body;

    public static SpendgridEmail from(ValidEmail email) {
        return SpendgridEmail.builder()
                .sender(email.getToName() + " <" + email.getToEmail() + ">")
                .recipient(email.getFromName() + " <" + email.getFromEmail() + ">")
                .subject(email.getSubject())
                .body(email.getBody())
                .build();
    }
}

//{
//  "sender": "brightwheel <noreply@mybrightwheel.com>",
//  "recipient": "Miss Susan <susan@abcpreschool.org>",
//  "subject": "Your Weekly Report",
//  "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>"
//}