package com.androidmage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditComment {

    private String body;
    private String created_utc;
    private int sentiment_score;
    private int hour;

    public RedditComment() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(String created_utc) {
        this.created_utc = created_utc;
        LocalDateTime date = Instant.ofEpochSecond(Long.parseLong(this.created_utc ))
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        this.setHour(date.getHour());
    }

    public int getSentiment_score() {
        return sentiment_score;
    }

    public void setSentiment_score(int sentiment_score) {
        this.sentiment_score = sentiment_score;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "body: " + body + ", created_utc: " + created_utc;
    }

}
