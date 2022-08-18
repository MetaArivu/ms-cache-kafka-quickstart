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
package io.fusion.air.microservice.adapters.security;

import io.fusion.air.microservice.domain.exceptions.AuthorizationException;
import io.fusion.air.microservice.security.JsonWebToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
@Aspect
public class AuthorizeRequestAspect {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    public final static String REFRESH_TOKEN = "Refresh-Token";
    public final static String AUTH_TOKEN = "Authorization";


    @Autowired
    private JsonWebToken jwtUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /**
     * Validate REST Endpoint Annotated with @validateRefreshToken Annotation
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(io.fusion.air.microservice.adapters.security.ValidateRefreshToken)")
    public Object validateRefreshRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return validateRequest(REFRESH_TOKEN, joinPoint);
    }

    /**
     * Validate REST Endpoints Annotated with @AuthorizationRequired Annotation
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(io.fusion.air.microservice.adapters.security.AuthorizationRequired)")
    public Object validateAnnotatedRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return validateRequest(AUTH_TOKEN, joinPoint);
    }

    /**
     * Secure All the REST Endpoints in the Secured Packaged using JWT
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* io.fusion.air.microservice.adapters.controllers.secured.*.*(..))")
    public Object validateAnyRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return validateRequest(AUTH_TOKEN,joinPoint);
    }

    /**
     * Validate the Request
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private Object validateRequest(String tokenKey, ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the request object
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logTime(startTime, "Validating", request.getRequestURI(), joinPoint);

        final String token = getToken(startTime, request.getHeader(tokenKey), joinPoint);
        final String user = getUser(startTime, token, joinPoint);
        // Validate the Token when User is NOT Null and Security Context = NULL
        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate Token
            UserDetails userDetails = validateToken(startTime, user, tokenKey, token, joinPoint);
            UsernamePasswordAuthenticationToken authorizeToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authorizeToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the Security Context with current user as Authorized for the request,
            // So it passes the Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(authorizeToken);
            logTime(startTime, "Success", "User Authorized for the request",  joinPoint);
        } else {
            logTime(startTime, "Success", "Security is already set!",  joinPoint);

        }
        return joinPoint.proceed();
    }

    /**
     * Extract the Token fromm the Authorization Header
     * ------------------------------------------------------------------------------------------------------
     * Authorization: Bearer AAA.BBB.CCC
     * ------------------------------------------------------------------------------------------------------
     * @param tokenKey
     * @return
     */
    private String getToken(long startTime, String tokenKey, ProceedingJoinPoint joinPoint) {
        if (tokenKey != null && tokenKey.startsWith("Bearer ")) {
            return tokenKey.substring(7);
        }
        String msg = "Access Denied: Unable to extract token from Header!";
        logTime(startTime, "Error", msg,  joinPoint);
        throw new AuthorizationException(msg);
    }

    /**
     * Returns the user from the Token
     * @param token
     * @return
     */
    private String getUser(long startTime, String token, ProceedingJoinPoint joinPoint) {
        String user = null;
        String msg = null;
        try {
            user = jwtUtil.getSubjectFromToken(token);
            // Store the user info for logging
            MDC.put("user", user);
            return user;
        } catch (IllegalArgumentException e) {
            msg = "Access Denied: Unable to get JWT Token Error: "+e.getMessage();
            throw new AuthorizationException(msg);
        } catch (ExpiredJwtException e) {
            msg = "Access Denied: JWT Token has expired Error: "+e.getMessage();
            throw new AuthorizationException(msg);
        } catch (NullPointerException e) {
            msg = "Access Denied: Invalid Token (Null Token) Error: "+e.getMessage();
            throw new AuthorizationException(msg);
        } catch (Throwable e) {
            msg = "Access Denied: Error:  "+e.getMessage();
            throw new AuthorizationException(msg);
        } finally {
            if(msg != null) {
                logTime(startTime, "Error", msg, joinPoint);
            }
        }
    }

    /**
     * Validate Token
     * - User
     * - Expiry Time
     * @param user
     * @param token
     * @param joinPoint
     * @return
     */
    private UserDetails validateToken(long startTime, String user, String tokenKey,
                                      String token, ProceedingJoinPoint joinPoint) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);
        String msg = null;
        try {
            // Validate the Token
            if (jwtUtil.validateToken(userDetails.getUsername(), token)) {
                String role = jwtUtil.getUserRoleFromToken(token);
                // Verify that the user role name matches the role name defined by the protected resource
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                AuthorizationRequired annotation = signature.getMethod().getAnnotation(AuthorizationRequired.class);
                // If the User Role is Public then Skip the Role Check
                if (!role.trim().equalsIgnoreCase(UserRole.Public.toString()) && !annotation.role().equals(role)) {
                    msg = "Role check failed! User role:"+role+" - Allowed role "+annotation.role();
                    throw new AuthorizationException("Access Not authorized: Invalid Role!");
                }
                return userDetails;
            } else {
                msg = "Unauthorized Access: Validation Failed!";
                throw new AuthorizationException(msg);
            }
        } catch(Throwable e) {
            msg = "Unauthorized Access: Invalid Token! Error: "+e.getMessage();
            throw new AuthorizationException(msg);
        } finally {
            if(msg != null) {
                logTime(startTime, "Error", msg, joinPoint);
            }
        }
    }

    /**
     * Log Time
     * @param _startTime
     * @param _status
     * @param joinPoint
     */
    private void logTime(long _startTime, String _status, String _msg, ProceedingJoinPoint joinPoint) {
        long timeTaken=System.currentTimeMillis() - _startTime;
        log.info("2|JA|TIME={} ms|STATUS={}|CLASS={}|Msg={}", timeTaken, _status,joinPoint, _msg);
    }
}