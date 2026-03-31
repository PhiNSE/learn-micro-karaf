package com.example.microservicea.service;

import com.example.common.event.EventType;
import com.example.common.event.JobEvent;
import com.example.common.model.Job;
import com.example.microservicea.kafka.KafkaEventProducer;
import com.example.microservicea.redis.JobReadRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobServicesTest {

    @Test
    void createPublishesCreateEvent() {
        KafkaEventProducer producer = mock(KafkaEventProducer.class);
        JobCommandService service = new JobCommandService(producer);
        Job job = new Job("1", "Engineer", "Senior");

        service.create(job);

        ArgumentCaptor<JobEvent> captor = ArgumentCaptor.forClass(JobEvent.class);
        verify(producer).publish(captor.capture());
        assertEquals(EventType.CREATE, captor.getValue().getType());
        assertEquals("1", captor.getValue().getPayload().getId());
    }

    @Test
    void updatePublishesUpdateEventWithPathId() {
        KafkaEventProducer producer = mock(KafkaEventProducer.class);
        JobCommandService service = new JobCommandService(producer);
        Job job = new Job(null, "Manager", "Mid");

        service.update("88", job);

        ArgumentCaptor<JobEvent> captor = ArgumentCaptor.forClass(JobEvent.class);
        verify(producer).publish(captor.capture());
        assertEquals(EventType.UPDATE, captor.getValue().getType());
        assertEquals("88", captor.getValue().getPayload().getId());
    }

    @Test
    void queryReturnsJobFromRepository() {
        JobReadRepository repository = mock(JobReadRepository.class);
        when(repository.findById("2")).thenReturn(new Job("2", "DevOps", "Senior"));
        JobQueryService service = new JobQueryService(repository);

        Job found = service.getById("2");
        Job missing = service.getById("x");

        assertEquals("DevOps", found.getTitle());
        assertNull(missing);
    }
}
