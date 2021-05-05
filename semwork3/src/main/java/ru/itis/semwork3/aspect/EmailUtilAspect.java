package ru.itis.semwork3.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailUtilAspect {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * ru.itis.semwork3.util.EmailUtilImpl.sendMail(..))")
    public void callAtSendMail() { }

    @Around("callAtSendMail()")
    public Object aroundCallAtAllSendMail(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("Thread: " + Thread.currentThread().getName() + "; around (before proceeding) EmailUtil " + pjp.getTarget());
        return pjp.proceed();
    }

    @Before("callAtSendMail()")
    public void beforeCallAtAllSendMail(JoinPoint jp) {
        logger.info("Thread: " + Thread.currentThread().getName() + "; before EmailUtil " + jp.getTarget());
    }
}
