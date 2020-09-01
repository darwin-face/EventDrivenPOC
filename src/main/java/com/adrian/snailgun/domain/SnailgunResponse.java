package com.adrian.snailgun.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.time.Instant;

@Data
public class SnailgunResponse {
    String id;
    @JsonProperty("from_email")
    String from_email;
    @JsonProperty("from_name")
    String from_name;
    @JsonProperty("to_email")
    String to_email;
    @JsonProperty("to_name")
    String to_name;
    String subject;
    String status;
    String body;

    @JsonProperty("created_at")
    Instant created_at;
}

//{
//  "id": "snailgun_email_YShL8z2OWjpXmg9KO9OMrfP4",
//  "from_email": "noreply@mybrightwheel.com",
//  "from_name": "brightwheel",
//  "to_email": "susan@abcpreschool.org",
//  "to_name": "Miss Susan",
//  "subject": "Your Weekly Report",
//  "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>",
//  "status": "queued",
//  "created_at": "2020-10-23T16:22:24.518+00:00"
//}