package com.rahim.common.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Rahim Ahmed
 * @created 08/05/2024
 */
@Aspect
@Component
public abstract class AbstractHealthCheck {

    @Around("@annotation(com.rahim.common.config.aspect.HealthCheck)")
    public Object checkHealthAndExecute(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            if (method.isAnnotationPresent(HealthCheck.class)) {
                HealthCheck healthCheckAnnotation = method.getAnnotation(HealthCheck.class);
                String type = healthCheckAnnotation.type();

                if ("hazelcast".equals(type)) {
                    return handleHazelcast(method, joinPoint);
                } else if ("kafka".equals(type)) {
                    return handleKafka(joinPoint);
                }
            }
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected Object handleHazelcast(Method method, ProceedingJoinPoint joinPoint) {
        return null;
    }

    protected Object handleKafka(ProceedingJoinPoint joinPoint) {
        return null;
    }
}
