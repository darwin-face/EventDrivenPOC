package com.adrian.email.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class EmailRequest {
    @JsonProperty("to")
    String toEmail;
    @JsonProperty("to_name")
    String toName;
    @JsonProperty("from")
    String fromEmail;
    @JsonProperty("from_name")
    String fromName;
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