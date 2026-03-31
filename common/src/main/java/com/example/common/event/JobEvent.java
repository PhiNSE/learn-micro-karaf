package com.example.common.event;

import com.example.common.model.Job;

public class JobEvent {
    private String type;
    private Job payload;

    public JobEvent() {
    }

    public JobEvent(String type, Job payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Job getPayload() {
        return payload;
    }

    public void setPayload(Job payload) {
        this.payload = payload;
    }
}
