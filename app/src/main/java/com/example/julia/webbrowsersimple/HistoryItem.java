package com.example.julia.webbrowsersimple;

/**
 * Created by julia on 21/10/2017.
 */

public class HistoryItem {

    private String history_url;
    private long history_timestamp;

    HistoryItem(String history_url, long history_timestamp) {
        this.history_url = history_url;
        this.history_timestamp = history_timestamp;
    }

    public String getHistory_url() {
        return history_url;
    }

    public long getHistory_timestamp() {
        return history_timestamp;
    }

    public void setHistory_url(String history_url) {
        this.history_url = history_url;
    }

    public void setHistory_timestamp(long history_timestamp) {
        this.history_timestamp = history_timestamp;
    }
}
