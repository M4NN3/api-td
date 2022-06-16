package com.softland.api.apitd.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class CustomMessage {
    private String message;
    private int code;
    private String status;
    //private String url;
    private ZonedDateTime timestamp;

    public CustomMessage() {
    }

    public CustomMessage(String message, int code, String status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public ZonedDateTime getTimestamp() {
        return ZonedDateTime.ofInstant(Instant.now(),  ZoneId.systemDefault());
    }
}
