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

import io.fusion.air.microservice.domain.exceptions.AbstractServiceException;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.exceptions.ResourceNotFoundException;
import io.fusion.air.microservice.domain.models.StandardResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
// @ControllerAdvice(basePackages="io.fusion.air.microservice.domain.exceptions")
// @ControllerAdvice(basePackages="io.fusion.air.microservice.*")
@RestControllerAdvice
@Order(2)
public class ServiceExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handle All Exceptions
     * @param _ex
     * @param _body
     * @param _headers
     * @param _status
     * @param _request
     * @return
     */
    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception _ex, Object _body, HttpHeaders _headers,
                                                          HttpStatus _status, WebRequest _request) {
        return createErrorResponse(_ex, _status, _request);
    }

    /**
     * Build Error Response Entity
     * @param _ex
     * @param _status
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Exception _ex, HttpStatus _status, WebRequest _request) {
        return createErrorResponse(_ex, _ex.getMessage(), "599",_status, _request);
    }

    /**
     * Build Error Response Entity
     * @param _ase
     * @param _errorCode
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(AbstractServiceException _ase,
                                                       String _errorCode, WebRequest _request) {
        return createErrorResponse(_ase, _ase.getMessage(), _errorCode, _ase.getHttpStatus(), _request);
    }
    /**
     * Build Standard Error Response
     * @param _exception
     * @param _message
     * @param _httpStatus
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Exception _exception, String _message, String _errorCode,
                                                       HttpStatus _httpStatus, WebRequest _request) {
        StandardResponse stdResponse = new StandardResponse();
        stdResponse.initFailure(_errorCode, _message);
        stdResponse.setPayload(_request.getContextPath());
        return new ResponseEntity<>(stdResponse, _httpStatus);
    }

    /**
     * InputDataException
     * @param _ase
     * @param _request
     * @return
     */
    @ExceptionHandler(value = InputDataException.class)
    public ResponseEntity<Object> standardException(InputDataException _ase, WebRequest _request) {
        return createErrorResponse(_ase, "410", _request);
    }

    /**v
     * Exception if the Request IS NOT FOUND!
     * @param _rnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException _rnfEx, WebRequest _request) {
        return createErrorResponse(_rnfEx, "404", _request);
    }

    /**
     * Access Denied Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException _adEx, WebRequest _request) {
        return createErrorResponse(_adEx, _adEx.getMessage(), "403", HttpStatus.FORBIDDEN, _request);
    }

    /**
     * Constraints Violation Exceptions
     * @param _cvEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException _cvEx, WebRequest _request) {

        StandardResponse stdResponse = new StandardResponse();
        stdResponse.initFailure( "401", "Input Errors");
        List<String> errors = new ArrayList<>();
        _cvEx.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        stdResponse.setPayload(errors);
        return new ResponseEntity<>(stdResponse, HttpStatus.BAD_REQUEST);
    }
}
