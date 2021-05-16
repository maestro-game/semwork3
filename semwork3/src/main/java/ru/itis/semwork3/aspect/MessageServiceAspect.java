package ru.itis.semwork3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.message.InnerMessageDto;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class MessageServiceAspect {
    @Pointcut(value = "execution(public java.util.Optional<ru.itis.semwork3.dto.message.InnerMessageDto> ru.itis.semwork3.service.MessageServiceImpl.*(..))")
    public void callAtAllPublic() { }

    @Around(value = "callAtAllPublic()")
    public Object aroundCallAtAllPublic(ProceedingJoinPoint pjp) throws Throwable {
        String args = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        log.info("Thread: " + Thread.currentThread().getName() + "; before MessageService " + pjp.getTarget() + ", args=[" + args + "]");
        Optional<InnerMessageDto> ret = (Optional<InnerMessageDto>) pjp.proceed();
        log.info(ret.map(innerMessageDto -> "assigned id: " + innerMessageDto.getId().toString()).orElse("was returned empty Optional"));
        return ret;
    }
}
