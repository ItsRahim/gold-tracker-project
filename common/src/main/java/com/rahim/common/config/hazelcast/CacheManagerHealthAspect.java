//package com.rahim.common.config.hazelcast;
//
//import com.rahim.common.service.hazelcast.IFallbackService;
//import com.rahim.common.service.hazelcast.implementation.HazelcastCacheManager;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * @author Rahim Ahmed
// * @created 06/05/2024
// */
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class CacheManagerHealthAspect {
//
//    private static final Logger LOG = LoggerFactory.getLogger(CacheManagerHealthAspect.class);
//    private final IFallbackService fallbackService;
//
//    //@Before("execution(* com.rahim.common.service.hazelcast.implementation.HazelcastCacheManager.*(..))")
//    @Before("execution(* com.rahim.common.service.hazelcast.implementation.HazelcastCacheManager.*(..))")
//    public void checkCacheManagerHealth(HazelcastCacheManager cacheManager, JoinPoint joinPoint) {
//        if (!cacheManager.isHealthy()) {
//            String methodName = joinPoint.getSignature().getName();
//            Object[] args = joinPoint.getArgs();
//
//            switch (methodName) {
//                case "getSet":
//                    String setNameGet = (String) args[0];
//                    fallbackService.getSet(setNameGet);
//                    break;
//                case "addToSet":
//                    String setNameAdd = (String) args[0];
//                    Object valueAdd = args[1];
//                    fallbackService.addToSet(setNameAdd, valueAdd);
//                    break;
//                case "removeFromSet":
//                    String setNameRemove = (String) args[0];
//                    Object valueRemove = args[1];
//                    fallbackService.removeFromSet(setNameRemove, valueRemove);
//                    break;
//                case "clearSet":
//                    String setNameClear = (String) args[0];
//                    fallbackService.clearSet(setNameClear);
//                    break;
//                case "getMap":
//                    String mapNameGet = (String) args[0];
//                    fallbackService.getMap(mapNameGet);
//                    break;
//                case "addToMap":
//                    String mapNameAdd = (String) args[0];
//                    String keyAdd = (String) args[1];
//                    String valueAddMap = (String) args[2];
//                    fallbackService.addToMap(mapNameAdd, keyAdd, valueAddMap);
//                    break;
//                case "removeFromMap":
//                    String mapNameRemove = (String) args[0];
//                    String keyRemove = (String) args[1];
//                    fallbackService.removeFromMap(mapNameRemove, keyRemove);
//                    break;
//                case "clearMap":
//                    String mapNameClear = (String) args[0];
//                    fallbackService.clearMap(mapNameClear);
//                    break;
//                default:
//                    LOG.error("Unknown method call");
//                    break;
//            }
//        }
//    }
//}
