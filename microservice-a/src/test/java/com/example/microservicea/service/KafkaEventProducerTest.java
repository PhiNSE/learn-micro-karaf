package com.example.microservicea.service;

import com.example.common.event.JsonUtil;
import com.example.common.event.JobEvent;
import com.example.common.model.Job;
import com.example.microservicea.kafka.KafkaEventProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KafkaEventProducerTest {
    @Test
    void publishSerializesEventAndSendsRecord() {
        KafkaProducer<String, String> kafkaProducer = mock(KafkaProducer.class);
        KafkaEventProducer producer = new KafkaEventProducer(kafkaProducer, new JsonUtil(), "job-events");

        producer.publish(new JobEvent("CREATE", new Job("7", "Engineer", "Junior")));

        ArgumentCaptor<ProducerRecord<String, String>> captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaProducer).send(captor.capture());
        assertEquals("job-events", captor.getValue().topic());
        assertEquals("7", captor.getValue().key());
    }
}
