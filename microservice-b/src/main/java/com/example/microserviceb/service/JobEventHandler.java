package com.example.microserviceb.service;

import com.example.common.event.EventType;
import com.example.common.event.JobEvent;
import com.example.microserviceb.redis.JobWriteRepository;

public class JobEventHandler {
    private final JobWriteRepository repository;

    public JobEventHandler(JobWriteRepository repository) {
        this.repository = repository;
    }

    public void handle(JobEvent event) {
        if (event == null || event.getPayload() == null) {
            return;
        }
        if (EventType.CREATE.equals(event.getType()) || EventType.UPDATE.equals(event.getType())) {
            repository.createOrUpdate(event.getPayload());
        } else if (EventType.DELETE.equals(event.getType())) {
            repository.delete(event.getPayload().getId());
        }
    }
}
