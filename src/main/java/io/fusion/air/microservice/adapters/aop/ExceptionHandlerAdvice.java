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

import io.fusion.air.microservice.domain.exceptions.*;
import io.fusion.air.microservice.domain.models.StandardResponse;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.MDC;


import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@ControllerAdvice
@Order(2)
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    // ServiceConfiguration
    @Autowired
    private ServiceConfiguration serviceConfig;

    /**
     * Handle All Exceptions
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
                Exception ex, @Nullable Object body,
                HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        return createErrorResponse(ex, headers, status, request);
    }

    /**
     * Build Error Response Entity
     * @param _ex
     * @param _status
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Exception _ex, HttpStatus _status, WebRequest _request) {
        return createErrorResponse(_ex, _ex.getMessage(), "599",null, _status, _request);
    }
    /**
     * Build Error Response Entity
     * @param _ex
     * @param _headers
     * @param _status
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Exception _ex, HttpHeaders _headers,
                                                       HttpStatus _status, WebRequest _request) {
        return createErrorResponse(_ex, _ex.getMessage(), "599",_headers, _status, _request);
    }

    /**
     * Build Error Response Entity
     * @param _ase
     * @param _errorCode
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(AbstractServiceException _ase, HttpHeaders _headers,
                                                       String _errorCode, WebRequest _request) {
        return createErrorResponse(_ase, _ase.getMessage(), _errorCode, _headers, _ase.getHttpStatus(), _request);
    }
    /**
     * Build Standard Error Response
     * @param _exception
     * @param _message
     * @param _httpStatus
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Throwable _exception, String _message, String _errorCode,
                                                       HttpHeaders _headers, HttpStatus _httpStatus, WebRequest _request) {

        String errorPrefix = (serviceConfig != null) ? serviceConfig.getServiceAPIErrorPrefix() : "AK";
        /**
        StandardResponse stdResponse = new StandardResponse();
        stdResponse.initFailure(errorPrefix + _errorCode, _message);

        LinkedHashMap<String, LinkedHashMap> payload = new LinkedHashMap<String,LinkedHashMap>();
        LinkedHashMap<String,Object> errorData = new LinkedHashMap<String,Object>();
        errorData.put("code", _httpStatus.value());
        errorData.put("mesg", _httpStatus.name());
        errorData.put("srv", MDC.get("Service"));
        errorData.put("reqId", MDC.get("ReqId"));
        errorData.put("http", MDC.get("Protocol"));
        errorData.put("path", MDC.get("URI"));
        payload.put("errors", errorData);
        stdResponse.setPayload(payload);
        */
        StandardResponse stdResponse = Utils.createErrorResponse(
                null, errorPrefix, _httpStatus, "400", _message);
        if(_headers != null) {
            return new ResponseEntity<>(stdResponse, _headers, _httpStatus);
        }
        return new ResponseEntity<>(stdResponse, _httpStatus);
    }

    /**
     * Handle Runtime Exception
     * @param _runEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> runtimeException(RuntimeException _runEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_runEx, _runEx.getMessage(), "590", _headers, HttpStatus.INTERNAL_SERVER_ERROR, _request);
    }

    /**
     * Handle Any Exception
     * @param _runEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> throwable(Throwable _runEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_runEx, _runEx.getMessage(), "599", _headers, HttpStatus.INTERNAL_SERVER_ERROR, _request);
    }

    /**
     * Access Denied Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException _adEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_adEx, _adEx.getMessage(), "403", _headers, HttpStatus.FORBIDDEN, _request);
    }

    /**v
     * Exception if the Request IS NOT FOUND!
     * @param _rnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException _rnfEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_rnfEx, _headers, "404", _request);
    }

    /**
     * Messaging Exception
     * @param _msgEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<Object> handleMessagingException(MessagingException _msgEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_msgEx, _headers, "430", _request);
    }

    /**
     * Database Exception
     * @param _dbEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DatabaseException.class)
    public ResponseEntity<Object> handleDatabaseException(DatabaseException _dbEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_dbEx, _headers, "440", _request);
    }

    /**
     * Data Not Found Exception
     * @param _dnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException _dnfEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_dnfEx, _headers, "441", _request);
    }

    /**
     * Duplicate Data Exception
     * @param _ddEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DuplicateDataException.class)
    public ResponseEntity<Object> handleDuplicateDataException(DuplicateDataException _ddEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_ddEx, _headers, "442", _request);
    }

    /**
     * Unable to Save Exception
     * @param _utEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = UnableToSaveException.class)
    public ResponseEntity<Object> handleUnableToSaveException(UnableToSaveException _utEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_utEx, _headers, "443", _request);
    }

    /**
     * Business Exception
     * @param _buEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = BusinessServiceException.class)
    public ResponseEntity<Object> handleBusinessServiceException(BusinessServiceException _buEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_buEx, _headers, "460", _request);
    }

    /**
     * InputDataException
     * @param _idEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = InputDataException.class)
    public ResponseEntity<Object> handleInputDataException(InputDataException _idEx,  WebRequest _request) {
        return createErrorResponse(_idEx, null,"461", _request);
    }

    /**
     * Mandatory Data Required Exception
     * @param _mdrEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MandatoryDataRequiredException.class)
    public ResponseEntity<Object> handleMandatoryDataRequiredException(MandatoryDataRequiredException _mdrEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_mdrEx, _headers, "462", _request);
    }


    /**
     * Controller Exception
     * @param _coEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ControllerException.class)
    public ResponseEntity<Object> handleControllerException(ControllerException _coEx, HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_coEx, _headers, "490", _request);
    }

    /**
     * Constraints Violation Exceptions
     * @param _cvEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException _cvEx, HttpHeaders _headers, WebRequest _request) {

        String errorPrefix = (serviceConfig != null) ? serviceConfig.getServiceAPIErrorPrefix() : "AK";
        List<String> errors = new ArrayList<>();
        _cvEx.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

        StandardResponse stdResponse = Utils.createErrorResponse(
                errors, errorPrefix, HttpStatus.BAD_REQUEST, "401", "Input Errors");
        return new ResponseEntity<>(stdResponse, _headers, HttpStatus.BAD_REQUEST);
    }
}
