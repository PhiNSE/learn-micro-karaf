package com.example.microserviceb.redis;

import com.example.common.model.Person;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class PersonRepository {
    private final RedissonClient redissonClient;

    public PersonRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void save(Person person) {
        RBucket<Person> bucket = redissonClient.getBucket("person:" + person.getId());
        bucket.set(person);
    }

    public Person findById(String id) {
        return redissonClient.<Person>getBucket("person:" + id).get();
    }
}
