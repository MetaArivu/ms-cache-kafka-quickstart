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

    @Autowired
    private JsonWebToken jwtUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /**
     * Validate REST Endpoints Annotated with @AuthorizationRequired Annotation
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(io.fusion.air.microservice.adapters.security.AuthorizationRequired)")
    public Object validateAnnotatedRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return validateRequest(joinPoint);
    }

    /**
     * Secure All the REST Endpoints in the Secured Packaged using JWT
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* io.fusion.air.microservice.adapters.controllers.secured.*.*(..))")
    public Object validateAnyRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return validateRequest(joinPoint);
    }

    /**
     * Validate the Request
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private Object validateRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        // Get the request object
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        final String tokenHeader = request.getHeader("Authorization");
        String user = null, token = null;
        log.info("|JwtAspect|Validating Request > {}", request.getRequestURI());
        /**
         * Extract the Token fromm the Authorization Header
         * ------------------------------------------------------------------------------------------------------
         Authorization: Bearer AAA.BBB.CCC
         * ------------------------------------------------------------------------------------------------------
         */
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                user = jwtUtil.getSubjectFromToken(token);
                // Store the user info for logging
                MDC.put("user", user);
            } catch (IllegalArgumentException e) {
                log.error("|JwtAspect|Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("|JwtAspect|JWT Token has expired");
            }
        } else {
            log.warn("|JwtAspect|JWT Token does not begin with Bearer String");
            log.error("|JwtAspect|Unauthorized Access to > {}", request.getRequestURI());
            throw new AuthorizationException("Access Not authorized: No Token Available!");
        }
        // Validate the Token when User is NOT Null and Security Context = NULL
        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user);
            // Validate the Token
            if (jwtUtil.validateToken(userDetails.getUsername(), token)) {
                String role = jwtUtil.getUserRoleFromToken(token);

                // Verify that the user role name matches the role name defined by the protected resource
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                AuthorizationRequired annotation = signature.getMethod().getAnnotation(AuthorizationRequired.class);
                // If the User Role is Public then Skip the Role Check
                if (!role.trim().equalsIgnoreCase(UserRole.Public.toString()) && !annotation.role().equals(role)) {
                    log.warn("|JwtAspect|Role check failed! User role:{} - Allowed role {}", role, annotation.role());
                    throw new AuthorizationException("Access Not authorized: Invalid Role!");
                }
            } else {
                log.error("|JwtAspect|Unauthorized Access to > {}", request.getRequestURI());
                throw new AuthorizationException("Access Not authorized: Invalid Token!");
            }
            UsernamePasswordAuthenticationToken authorizeToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authorizeToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the Security Context with current user as Authorized for the request,
            // So it passes the Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(authorizeToken);
            log.info("|JwtAspect|User Authorized for the request:");
        } else {
            log.info("|JwtAspect|Security is already set!");
        }
        return joinPoint.proceed();
    }
}