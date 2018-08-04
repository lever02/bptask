package com.bigpanda.interview.entities;

import com.bigpanda.interview.utils.UnixTimestampDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

public class PandaEvent {
    @JsonProperty("event_type")
    private String type;
    @JsonProperty("data")
    private String data;
    @JsonProperty("timestamp")
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private Date date;

    public PandaEvent() {
    }

    public PandaEvent(String type, String data, Date date) {
        this.type = type;
        this.data = data;
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PandaEvent{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", date=" + date +
                '}';
    }

    public boolean isEmptyPandaEvent() {
        if (this.data == null && this.type == null && this.date == null) {
            return false;
        }
        return true;
    }
}
