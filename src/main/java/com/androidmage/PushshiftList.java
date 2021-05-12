package com.androidmage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushshiftList {

    private List<RedditComment> data;

    public PushshiftList() {
    }

    public List<RedditComment> getData() {
        return data;
    }

    public void setData(List<RedditComment> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String str = "data:";
        for(RedditComment comment : data) {
            str = str + "\n" + comment.toString();
        }
        return str;
    }
}
