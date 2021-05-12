package com.androidmage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditComment {

    private String body;
    private String created_utc;
    private int score;

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
    }

    @Override
    public String toString() {
        return "body: " + body + ", created_utc: " + created_utc;
    }

}
