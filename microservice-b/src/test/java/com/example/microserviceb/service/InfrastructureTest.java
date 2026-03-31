package com.example.microserviceb.service;

import com.example.common.event.JsonUtil;
import com.example.common.event.JobEvent;
import com.example.common.model.Job;
import com.example.common.model.Person;
import com.example.microserviceb.kafka.JobEventConsumer;
import com.example.microserviceb.redis.JobWriteRepository;
import com.example.microserviceb.redis.PersonRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InfrastructureTest {
    @Test
    void writeRepositoryUsesJobKey() {
        RedissonClient redisson = mock(RedissonClient.class);
        RBucket<Job> bucket = mock(RBucket.class);
        when(redisson.<Job>getBucket("job:1")).thenReturn(bucket);
        JobWriteRepository repository = new JobWriteRepository(redisson);

        repository.createOrUpdate(new Job("1", "Dev", "Junior"));
        repository.delete("1");

        verify(bucket).set(any(Job.class));
        verify(bucket).delete();
    }

    @Test
    void personRepositoryUsesPersonKey() {
        RedissonClient redisson = mock(RedissonClient.class);
        RBucket<Person> bucket = mock(RBucket.class);
        when(redisson.<Person>getBucket("person:p1")).thenReturn(bucket);
        PersonRepository repository = new PersonRepository(redisson);
        Person person = new Person("p1", "Alex", 30, "j1");

        repository.save(person);
        repository.findById("p1");

        verify(bucket).set(person);
        verify(bucket).get();
    }

    @Test
    void eventConsumerPollsAndDelegates() {
        KafkaConsumer<String, String> kafkaConsumer = mock(KafkaConsumer.class);
        JobEventHandler handler = mock(JobEventHandler.class);
        JobEvent event = new JobEvent("CREATE", new Job("1", "Eng", "Mid"));

        TopicPartition partition = new TopicPartition("job-events", 0);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("job-events", 0, 0, "1", new JsonUtil().toJson(event));
        Map<TopicPartition, java.util.List<ConsumerRecord<String, String>>> recordsMap = new HashMap<>();
        recordsMap.put(partition, Collections.singletonList(record));
        ConsumerRecords<String, String> records = new ConsumerRecords<>(recordsMap);

        JobEventConsumer consumer = new JobEventConsumer(kafkaConsumer, new JsonUtil(), handler, "job-events");
        when(kafkaConsumer.poll(any())).thenAnswer(invocation -> {
            consumer.stop();
            return records;
        });
        consumer.run();

        verify(handler).handle(any(JobEvent.class));
        verify(kafkaConsumer).close();
    }

    @Test
    void clientFactoryBuildsKafkaConsumer() {
        ClientFactory factory = new ClientFactory();
        KafkaConsumer<String, String> consumer = factory.createKafkaConsumer("localhost:9092", "group-a");
        assertNotNull(consumer);
        consumer.close();
    }
}
