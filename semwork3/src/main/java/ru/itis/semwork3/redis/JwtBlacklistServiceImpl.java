package ru.itis.semwork3.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {
    private final RedisRepository redisRepository;

    @Override
    public void add(String token) {
        redisRepository.save(token);
    }

    @Override
    public boolean exists(String token) {
        return redisRepository.exists(token);
    }
}
