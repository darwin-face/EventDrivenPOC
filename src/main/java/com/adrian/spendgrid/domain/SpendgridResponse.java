package com.adrian.spendgrid.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpendgridResponse {
    String id;
    String sender;
    String recipient;
    String subject;
    String body;
    String created_at;
}

//{
//        "id": "spendgrid_email_6CqxFJB8CJI9BHZTXwbDtYXU",
//        "sender": "brightwheel <noreply@mybrightwheel.com>",
//        "recipient": "Miss Susan <susan@abcpreschool.org>",
//        "subject": "Your Weekly Report",
//        "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>",
//        "created_at": "2021-04-18T21:30:16.196+00:00"
//}