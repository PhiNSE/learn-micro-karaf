package com.example.microservicea.service;

import com.example.common.model.Job;
import com.example.microservicea.redis.JobReadRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfrastructureTest {
    @Test
    void readRepositoryUsesJobKey() {
        RedissonClient redisson = mock(RedissonClient.class);
        RBucket<Job> bucket = mock(RBucket.class);
        when(redisson.<Job>getBucket("job:abc")).thenReturn(bucket);
        when(bucket.get()).thenReturn(new Job("abc", "SRE", "Senior"));
        JobReadRepository repository = new JobReadRepository(redisson);

        Job job = repository.findById("abc");

        assertEquals("SRE", job.getTitle());
    }

    @Test
    void clientFactoryBuildsKafkaProducer() {
        ClientFactory factory = new ClientFactory();
        KafkaProducer<String, String> producer = factory.createKafkaProducer("localhost:9092");
        assertNotNull(producer);
        producer.close();
    }
}
