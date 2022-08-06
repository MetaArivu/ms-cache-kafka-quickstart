/**
 * (C) Copyright 2022 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.adapters.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Log Aspect
 * Log Messages
 * Keep Track of Time
 * Exception Handling
 *
 * @author arafkarsh
 */
@Aspect
@Configuration
public class LogExceptionAspect {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    /**
     * Log Message before the Log Execution
     * @param joinPoint
     */
    @Before(value = "execution(* io.fusion.air.microservice.adapters.controllers.*.*(..))")
    public void logStatementBefore(JoinPoint joinPoint) {
        log.debug("START={}",joinPoint);
    }

    /**
     * Log Message after the Method Execution
     * @param joinPoint
     */
    @After(value = "execution(* io.fusion.air.microservice.adapters.controllers.*.*(..))")
    public void logStatementAfter(JoinPoint joinPoint) {
        log.debug("END={}",joinPoint);
    }

    /**
     * Capture Method Execution Time
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* io.fusion.air.microservice.adapters.controllers.*.*(..))")
    public Object timeTracker(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object method = joinPoint.proceed();
            long timeTaken=System.currentTimeMillis() - startTime;
            log.info("TIME={} ms|SUCCESS=true|CLASS={}",timeTaken,joinPoint);
            return method;
        }catch(Throwable e) {
            long timeTaken=System.currentTimeMillis() - startTime;
            log.info("TIME={} ms|ERROR={}|CLASS={}",timeTaken, e.getMessage(),joinPoint);
            throw e;
        }
    }
}
