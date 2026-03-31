package com.example.microservicea.kafka;

import com.example.common.event.JsonUtil;
import com.example.common.event.JobEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaEventProducer {
    private final KafkaProducer<String, String> producer;
    private final JsonUtil jsonUtil;
    private final String topic;

    public KafkaEventProducer(KafkaProducer<String, String> producer, JsonUtil jsonUtil, String topic) {
        this.producer = producer;
        this.jsonUtil = jsonUtil;
        this.topic = topic;
    }

    public void publish(JobEvent event) {
        String value = jsonUtil.toJson(event);
        String key = event.getPayload() == null ? null : event.getPayload().getId();
        producer.send(new ProducerRecord<>(topic, key, value));
    }
}
