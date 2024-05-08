package com.rahim.common.config.health;

import com.rahim.common.service.hazelcast.HzFallbackService;
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
    private final HzFallbackService hzFallbackService;
    private volatile boolean isHzHealthy = true;
    private volatile boolean isKafkaHealthy = true;

    @Around("@annotation(com.rahim.common.config.health.HealthCheck)")
    public Object checkHealthAndExecute(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Class<?> returnType = signature.getReturnType();

            if (method.isAnnotationPresent(HealthCheck.class)) {
                HealthCheck healthCheckAnnotation = method.getAnnotation(HealthCheck.class);

                if ("hazelcast".equals(healthCheckAnnotation.type())) {
                    if (isHzHealthy) {
                        return joinPoint.proceed();
                    } else {
                        Object[] args = joinPoint.getArgs();
                        String methodName = method.getName();
                        return handleHazelcastFallbackCall(methodName, args, returnType);
                    }
                } else if ("kafka".equals(healthCheckAnnotation.type())) {
                    if (isKafkaHealthy) {
                        return joinPoint.proceed();
                    } else {
                        Object[] args = joinPoint.getArgs();
                        return handleKafkaFallback(args, returnType);
                    }
                } else {
                    LOG.error("Unknown type provided in annotation. Unable to process");
                }
            }
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Object handleHazelcastFallbackCall(String methodName, Object[] args, Class<?> returnType) {
        switch (methodName) {
            case "getSet", "addToSet", "removeFromSet", "clearSet":
                return handleSetOperations(methodName, args, returnType);
            case "getMap", "addToMap", "removeFromMap", "clearMap":
                return handleMapOperations(methodName, args, returnType);
            default:
                LOG.error("Unsupported method: {}", methodName);
                return null;
        }
    }

    private Object handleSetOperations(String methodName, Object[] args, Class<?> returnType) {
        if (args.length < 1 || !(args[0] instanceof String setName)) {
            LOG.error("Invalid parameters for {} fallback method", methodName);
            return null;
        }
        switch (methodName) {
            case "getSet":
                return executeFallback(() -> hzFallbackService.fallbackGetSet(setName), returnType);
            case "addToSet":
                if (args.length < 2 || args[1] == null) {
                    LOG.error("Value cannot be null for addToSet fallback method");
                    return null;
                }
                return executeFallback(() -> {
                    Object value = args[1];
                    hzFallbackService.fallbackAddToSet(setName, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "removeFromSet":
                if (args.length < 2) {
                    LOG.error("Invalid parameters for removeFromSet fallback method");
                    return null;
                }
                Object value = args[1];
                return executeFallback(() -> {
                    hzFallbackService.fallbackRemoveFromSet(setName, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "clearSet":
                return executeFallback(() -> {
                    hzFallbackService.fallbackClearSet(setName);
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
                return executeFallback(() -> hzFallbackService.fallbackGetMap(mapName), returnType);
            case "addToMap":
                if (args.length < 3 || args[1] == null) {
                    LOG.error("Key or value cannot be null for addToMap fallback method");
                    return null;
                }
                String key = (String) args[1];
                Object value = args[2];
                return executeFallback(() -> {
                    hzFallbackService.fallbackAddToMap(mapName, key, value);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "removeFromMap":
                if (args.length < 2) {
                    LOG.error("Invalid parameters for removeFromMap fallback method");
                    return null;
                }
                key = (String) args[1];
                return executeFallback(() -> {
                    hzFallbackService.fallbackRemoveFromMap(mapName, key);
                    return getDefaultValueForReturnType(returnType);
                }, returnType);
            case "clearMap":
                return executeFallback(() -> {
                    hzFallbackService.fallbackClearMap(mapName);
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
        return null;
    }

}

