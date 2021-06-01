package ru.itis.semwork3.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.redis.RedisRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class MethodCountAspect {
    private final RedisRepository redisRepository;

    @Pointcut(value = "execution(public * ru.itis.semwork3.service.MessageServiceImpl.*(..)) ||" +
            "execution(public * ru.itis.semwork3.service.SignInServiceImpl.*(..)) ||" +
            "execution(public * ru.itis.semwork3.service.SignUpServiceImpl.*(..)) ||" +
            "execution(public * ru.itis.semwork3.service.ContentSourceServiceImpl.*(..)))")
    public void callAtAllPublicInService() { }

    @After(value = "callAtAllPublicInService()")
    public void callAfterAllPublic(JoinPoint jp) {
        redisRepository.increase(jp.getSignature().toString());
    }
}
