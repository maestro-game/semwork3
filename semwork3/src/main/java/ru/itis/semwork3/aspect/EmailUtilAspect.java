package ru.itis.semwork3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class EmailUtilAspect {
    @Pointcut("execution(public * ru.itis.semwork3.util.EmailUtilImpl.sendMail(..))")
    public void callAtSendMail() { }

    @Around("callAtSendMail()")
    public Object aroundCallAtAllSendMail(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Thread: " + Thread.currentThread().getName() + "; around (before proceeding) EmailUtil " + pjp.getTarget());
        return pjp.proceed();
    }

    @Before("callAtSendMail()")
    public void beforeCallAtAllSendMail(JoinPoint jp) {
        log.info("Thread: " + Thread.currentThread().getName() + "; before EmailUtil " + jp.getTarget());
    }
}
