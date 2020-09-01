package com.adrian.email.domain;

import lombok.Value;


@Value
public class Email {
    String to;
    String to_name;
    String from;
    String from_name;
    String subject;
    String body;
}

//      {
//          "to": "susan@abcpreschool.org",
//          "to_name": "Miss Susan",
//          "from": "noreply@mybrightwheel.com",
//          "from_name": "brightwheel",
//          "subject": "Your Weekly Report",
//          "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>"
//      }