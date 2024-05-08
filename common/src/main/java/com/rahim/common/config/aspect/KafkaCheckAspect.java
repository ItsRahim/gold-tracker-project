package com.rahim.common.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 07/05/2024
 */
@Setter
@Component
@RequiredArgsConstructor
public class KafkaCheckAspect extends AbstractHealthCheck{

    private static final Logger LOG = LoggerFactory.getLogger(KafkaCheckAspect.class);
    private final JdbcTemplate jdbcTemplate;
    private volatile boolean isHealthy = true;

    @Override
    protected Object handleKafka(ProceedingJoinPoint joinPoint) {
        try {
            if (isHealthy) {
                return joinPoint.proceed();
            } else {
                Object[] args = joinPoint.getArgs();
                return handleHazelcastFallbackCall(methodName, args, signature.getReturnType());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Object handleKafkaFallbackCall(Object[] args) {
        String topicName = (String) args[0];
        String data = (String) args[1];
        jdbcTemplate.execute("Select * from rgts.archived_users");
        return null;
    }

}
