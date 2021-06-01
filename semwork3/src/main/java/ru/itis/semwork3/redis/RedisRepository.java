package ru.itis.semwork3.redis;

public interface RedisRepository {
    void save(String token);

    void ban(String userId);

    boolean exists(String token);

    void increase(String methodSignature);
}
