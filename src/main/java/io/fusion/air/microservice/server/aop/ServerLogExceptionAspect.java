package io.fusion.air.microservice.server.aop;

import io.fusion.air.microservice.domain.exceptions.AbstractServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Log Aspect for the Servers
 * Log Messages
 * Keep Track of Time
 * Exception Handling
 *
 * @author arafkarsh
 */
@Aspect
@Configuration
public class ServerLogExceptionAspect {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    /**
     * Log Message before the Method Call
     * @param joinPoint
     */
    @Before(value = "execution(* io.fusion.air.microservice.server.controllers.*.*(..))")
    public void logStatementBefore(JoinPoint joinPoint) {
        log.info("API Call >> {}",joinPoint);
    }

    /**
     * Log Message after the method call
     * @param joinPoint
     */
    @After(value = "execution(* io.fusion.air.microservice.server.controllers.*.*(..))")
    public void logStatementAfter(JoinPoint joinPoint) {
        log.info("API Call >> Complete {}",joinPoint);
    }

    /**
     * Handle Exceptions
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* io.fusion.air.microservice.server.controllers.*.*(..))")
    public Object methodHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object method = joinPoint.proceed();
            return method;
        }catch(AbstractServiceException e) {
            log.info("ServiceException StatusCode {}",e.getHttpStatus().value());
            log.info("ServiceException Message {}",e.getMessage());
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
    }

    /**
     * Capture Method Execution Time
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* io.fusion.air.microservice.server.controllers.*.*(..))")
    public Object timeTracker(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object method = joinPoint.proceed();
            long timeTaken=System.currentTimeMillis() - startTime;
            log.info("Api Call >> Time taken by {} is {}",joinPoint,timeTaken);
            return method;
        }catch(AbstractServiceException e) {
            log.info("ServiceException StatusCode {}",e.getHttpStatus().value());
            log.info("ServiceException Message {}",e.getMessage());
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
    }

}
