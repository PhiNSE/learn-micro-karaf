package com.example.microserviceb.kafka;

import com.example.common.event.JsonUtil;
import com.example.common.event.JobEvent;
import com.example.microserviceb.service.JobEventHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

public class JobEventConsumer implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final JsonUtil jsonUtil;
    private final JobEventHandler handler;
    private final String topic;
    private volatile boolean running = true;

    public JobEventConsumer(KafkaConsumer<String, String> consumer, JsonUtil jsonUtil, JobEventHandler handler, String topic) {
        this.consumer = consumer;
        this.jsonUtil = jsonUtil;
        this.handler = handler;
        this.topic = topic;
    }

    public void start() {
        Thread thread = new Thread(this, "job-event-consumer");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(topic));
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, String> record : records) {
                JobEvent event = jsonUtil.fromJson(record.value(), JobEvent.class);
                handler.handle(event);
            }
        }
        consumer.close();
    }
}
