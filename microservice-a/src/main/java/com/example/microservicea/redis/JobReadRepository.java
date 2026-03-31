package com.example.microservicea.redis;

import com.example.common.model.Job;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class JobReadRepository {
    private final RedissonClient redissonClient;

    public JobReadRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Job findById(String id) {
        RBucket<Job> bucket = redissonClient.getBucket("job:" + id);
        return bucket.get();
    }
}
