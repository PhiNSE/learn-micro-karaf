package com.example.microservicea.service;

import com.example.common.model.Job;
import com.example.microservicea.redis.JobReadRepository;

public class JobQueryService {
    private final JobReadRepository repository;

    public JobQueryService(JobReadRepository repository) {
        this.repository = repository;
    }

    public Job getById(String id) {
        return repository.findById(id);
    }
}
