package com.example.microserviceb.redis;

import com.example.common.model.Job;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class JobWriteRepository {
    private final RedissonClient redissonClient;

    public JobWriteRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void createOrUpdate(Job job) {
        RBucket<Job> bucket = redissonClient.getBucket("job:" + job.getId());
        bucket.set(job);
    }

    public void delete(String id) {
        RBucket<Job> bucket = redissonClient.getBucket("job:" + id);
        bucket.delete();
    }
}
