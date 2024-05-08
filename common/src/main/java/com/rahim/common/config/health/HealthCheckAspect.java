package com.rahim.common.config.health;

import com.rahim.common.service.hazelcast.HazelcastFailover;
import com.rahim.common.service.kafka.KafkaFailover;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Rahim Ahmed
 * @created 07/05/2024
 */
@Aspect
@Setter
@Component
@RequiredArgsConstructor
public class HealthCheckAspect {

    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckAspect.class);
    private final HazelcastFailover hazelcastFailover;
    private final KafkaFailover kafkaFailover;
    private volatile boolean isHzHealthy = true;
    private volatile boolean isKafkaHealthy = true;

    private static final String HAZELCAST_TYPE = "hazelcast";
    private static final String KAFKA_TYPE = "kafka";

    @Around("@annotation(com.rahim.common.config.health.HealthCheck)")
    public Object checkHealthAndExecute(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            HealthCheck healthCheckAnnotation = method.getAnnotation(HealthCheck.class);

            if (healthCheckAnnotation == null) {
                return null;
            }

            String type = healthCheckAnnotation.type();
            Class<?> returnType = signature.getReturnType();
            Object[] args = joinPoint.getArgs();

            if (HAZELCAST_TYPE.equals(type) && !isHzHealthy) {
                return handleHazelcastFallback(args, method, returnType);
            } else if (KAFKA_TYPE.equals(type) && !isKafkaHealthy) {
                return handleKafkaFallback(args, returnType);
            } else {
                return joinPoint.proceed();
            }
        } catch (Throwable e) {
            LOG.error("An error has occurred attempting to process HealthCheck annotation", e);
            throw new RuntimeException(e);
        }
    }

    private Object handleHazelcastFallback(Object[] args, Method method, Class<?> returnType) {
        String methodName = method.getName();
        String operationType = getOperationType(methodName);

        if (operationType == null) {
            LOG.error("Unsupported method: {}", methodName);
            return null;
        }

        return switch (operationType) {
            case "set" -> handleSetOperations(methodName, args, returnType);
            case "map" -> handleMapOperations(methodName, args, returnType);
            default -> {
                LOG.error("Unsupported operation type: {}", operationType);
                yield null;
            }
        };
    }

    private String getOperationType(String methodName) {
        return switch (methodName) {
            case "getSet", "addToSet", "removeFromSet", "clearSet" -> "set";
            case "getMap", "addToMap", "removeFromMap", "clearMap" -> "map";
            default -> null;
        };
    }

    private Object handleSetOperations(String methodName, Object[] args, Class<?> returnType) {
        if (args.length < 1 || !(args[0] instanceof String setName)) {
            LOG.error("Invalid parameters for {} fallback method", methodName);
            return null;
        }
        switch (methodName) {
            case "getSet":
                return executeFallback(() -> hazelcastFailover.getSet(setName), returnType);
            case "addToSet":
                if (args.length < 2 || args[1] == null) {
                    LOG.error("Value cannot be null for addToSet fallback method");
                    return null;
                }
                return executeFallback(() -> {
                    Object value = args[1];
                    hazelcastFailover.addToSet(setName, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "removeFromSet":
                if (args.length < 2) {
                    LOG.error("Invalid parameters for removeFromSet fallback method");
                    return null;
                }
                Object value = args[1];
                return executeFallback(() -> {
                    hazelcastFailover.removeFromSet(setName, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "clearSet":
                return executeFallback(() -> {
                    hazelcastFailover.clearSet(setName);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            default:
                return null;
        }
    }

    private Object handleMapOperations(String methodName, Object[] args, Class<?> returnType) {
        if (args.length < 1 || !(args[0] instanceof String mapName)) {
            LOG.error("Invalid parameters for {} fallback method", methodName);
            return null;
        }
        switch (methodName) {
            case "getMap":
                return executeFallback(() -> hazelcastFailover.getMap(mapName), returnType);
            case "addToMap":
                if (args.length < 3 || args[1] == null) {
                    LOG.error("Key or value cannot be null for addToMap fallback method");
                    return null;
                }
                String key = (String) args[1];
                Object value = args[2];
                return executeFallback(() -> {
                    hazelcastFailover.addToMap(mapName, key, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "removeFromMap":
                if (args.length < 2) {
                    LOG.error("Invalid parameters for removeFromMap fallback method");
                    return null;
                }
                key = (String) args[1];
                return executeFallback(() -> {
                    hazelcastFailover.removeFromMap(mapName, key);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "clearMap":
                return executeFallback(() -> {
                    hazelcastFailover.clearMap(mapName);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            default:
                return null;
        }
    }

    private Object executeFallback(FallbackOperation operation, Class<?> returnType) {
        try {
            return operation.execute();
        } catch (Exception e) {
            LOG.error("Error during health check: {}", e.getMessage());
            return getDefaultValueForReturnType(returnType);
        }
    }

    private Object getDefaultValueForReturnType (Class < ? > returnType){
        if (returnType == int.class || returnType == Integer.class) {
            return 0;
        } else if (returnType == boolean.class || returnType == Boolean.class) {
            return false;
        } else if (returnType == double.class || returnType == Double.class) {
            return 0.0;
        }
        return null;
    }

    private interface FallbackOperation {
        Object execute();
    }

    private Object handleKafkaFallback(Object[] args, Class<?> returnType) {
        String topic = (String) args[0];
        String data = (String) args[1];
        LOG.debug("Fallback triggered for Kafka send operation. Topic: {}", topic);

        kafkaFailover.persistToDb(topic, data);
        return getDefaultValueForReturnType(returnType);
    }

}
