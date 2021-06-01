package ru.itis.semwork3.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryTemplateImpl implements RedisRepository {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String token) {
        redisTemplate.opsForValue().set(token, "", Duration.of(90, ChronoUnit.DAYS));
    }

    @Override
    public void ban(String userId) {
        redisTemplate.opsForValue().set(userId, "");
    }

    @Override
    public boolean exists(String token) {
        return Boolean.TRUE == redisTemplate.hasKey(token);
    }

    @Override
    public void increase(String methodSignature) {
        redisTemplate.opsForHash().increment("methods", methodSignature, 1);
    }
}
