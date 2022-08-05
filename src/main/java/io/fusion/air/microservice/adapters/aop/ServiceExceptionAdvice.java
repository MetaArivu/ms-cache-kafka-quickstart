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
@ControllerAdvice
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
     * Access Denied Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException _adEx, WebRequest _request) {
        return createErrorResponse(_adEx, _adEx.getMessage(), "403", HttpStatus.FORBIDDEN, _request);
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
     * Messaging Exception
     * @param _msgEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<Object> messagingException(MessagingException _msgEx, WebRequest _request) {
        return createErrorResponse(_msgEx, "430", _request);
    }

    /**
     * Database Exception
     * @param _dbEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DatabaseException.class)
    public ResponseEntity<Object> databaseException(DatabaseException _dbEx, WebRequest _request) {
        return createErrorResponse(_dbEx, "440", _request);
    }

    /**
     * Data Not Found Exception
     * @param _dnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> dataNotFoundException(DataNotFoundException _dnfEx, WebRequest _request) {
        return createErrorResponse(_dnfEx, "441", _request);
    }

    /**
     * Duplicate Data Exception
     * @param _ddEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DuplicateDataException.class)
    public ResponseEntity<Object> duplicateDataException(DuplicateDataException _ddEx, WebRequest _request) {
        return createErrorResponse(_ddEx, "442", _request);
    }

    /**
     * Unable to Save Exception
     * @param _utEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = UnableToSaveException.class)
    public ResponseEntity<Object> unableToSaveException(UnableToSaveException _utEx, WebRequest _request) {
        return createErrorResponse(_utEx, "443", _request);
    }

    /**
     * Business Exception
     * @param _buEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = BusinessServiceException.class)
    public ResponseEntity<Object> businessServiceException(BusinessServiceException _buEx, WebRequest _request) {
        return createErrorResponse(_buEx, "460", _request);
    }

    /**
     * InputDataException
     * @param _idEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = InputDataException.class)
    public ResponseEntity<Object> inputDataException(InputDataException _idEx, WebRequest _request) {
        return createErrorResponse(_idEx, "461", _request);
    }

    /**
     * Mandatory Data Required Exception
     * @param _mdrEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MandatoryDataRequiredException.class)
    public ResponseEntity<Object> inputDataException(MandatoryDataRequiredException _mdrEx, WebRequest _request) {
        return createErrorResponse(_mdrEx, "462", _request);
    }


    /**
     * Controller Exception
     * @param _coEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ControllerException.class)
    public ResponseEntity<Object> inputDataException(ControllerException _coEx, WebRequest _request) {
        return createErrorResponse(_coEx, "490", _request);
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
