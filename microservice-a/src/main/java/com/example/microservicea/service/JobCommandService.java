package com.example.microservicea.service;

import com.example.common.event.EventType;
import com.example.common.event.JobEvent;
import com.example.common.model.Job;
import com.example.microservicea.kafka.KafkaEventProducer;

public class JobCommandService {
    private final KafkaEventProducer eventProducer;

    public JobCommandService(KafkaEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public void create(Job job) {
        eventProducer.publish(new JobEvent(EventType.CREATE, job));
    }

    public void update(String id, Job job) {
        job.setId(id);
        eventProducer.publish(new JobEvent(EventType.UPDATE, job));
    }

    public void delete(String id) {
        eventProducer.publish(new JobEvent(EventType.DELETE, new Job(id, null, null)));
    }
}
