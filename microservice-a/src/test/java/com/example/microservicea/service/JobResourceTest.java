package com.example.microservicea.service;

import com.example.common.model.Job;
import com.example.microservicea.api.JobResource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobResourceTest {
    @Test
    void getReturnsNotFoundWhenMissing() {
        JobCommandService command = mock(JobCommandService.class);
        JobQueryService query = mock(JobQueryService.class);
        when(query.getById("1")).thenReturn(null);
        JobResource resource = new JobResource(command, query);

        Response response = resource.get("1");

        assertEquals(404, response.getStatus());
    }

    @Test
    void createReturnsAcceptedAndDelegates() {
        JobCommandService command = mock(JobCommandService.class);
        JobQueryService query = mock(JobQueryService.class);
        JobResource resource = new JobResource(command, query);
        Job job = new Job("1", "Tester", "Mid");

        Response response = resource.create(job);

        assertEquals(202, response.getStatus());
        verify(command).create(job);
    }
}
