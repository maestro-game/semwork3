package ru.itis.semwork3.redis;

public interface JwtBlacklistService {
    void add(String token);

    boolean exists(String token);
}
