package com.example.microserviceb.service;

import com.example.common.event.EventType;
import com.example.common.event.JobEvent;
import com.example.common.model.Job;
import com.example.microserviceb.redis.JobWriteRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.times;

class JobEventHandlerTest {
    @Test
    void createAndUpdateWriteToRedis() {
        JobWriteRepository repository = mock(JobWriteRepository.class);
        JobEventHandler handler = new JobEventHandler(repository);
        Job job = new Job("55", "Architect", "Lead");

        handler.handle(new JobEvent(EventType.CREATE, job));
        handler.handle(new JobEvent(EventType.UPDATE, job));

        verify(repository, times(2)).createOrUpdate(job);
    }

    @Test
    void deleteRemovesFromRedis() {
        JobWriteRepository repository = mock(JobWriteRepository.class);
        JobEventHandler handler = new JobEventHandler(repository);

        handler.handle(new JobEvent(EventType.DELETE, new Job("99", null, null)));

        verify(repository).delete("99");
    }

    @Test
    void nullPayloadIsIgnored() {
        JobWriteRepository repository = mock(JobWriteRepository.class);
        JobEventHandler handler = new JobEventHandler(repository);

        handler.handle(new JobEvent(EventType.CREATE, null));

        verifyNoInteractions(repository);
    }
}
