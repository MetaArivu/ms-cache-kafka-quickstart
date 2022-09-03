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
import io.fusion.air.microservice.domain.exceptions.SecurityException;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
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
    private ResponseEntity<Object> createErrorResponse(AbstractServiceException _ase,
                                                       String _errorCode, WebRequest _request) {
        return createErrorResponse(_ase, _ase.getMessage(), _errorCode, null, _ase.getHttpStatus(), _request);
    }
    /**
     * Build Error Response Entity
     * @param _ase
     * @param _errorCode
     * @param _headers
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(AbstractServiceException _ase, String _errorCode,
                                                       HttpHeaders _headers, WebRequest _request) {
        return createErrorResponse(_ase, _ase.getMessage(), _errorCode, _headers, _ase.getHttpStatus(), _request);
    }

    /**
     * Build Standard Error Response
     * @param _exception
     * @param _message
     * @param _errorCode
     * @param _headers
     * @param _httpStatus
     * @param _request
     * @return
     */
    private ResponseEntity<Object> createErrorResponse(Throwable _exception, String _message, String _errorCode,
                                                       HttpHeaders _headers, HttpStatus _httpStatus, WebRequest _request) {

        String errorPrefix = (serviceConfig != null) ? serviceConfig.getServiceAPIErrorPrefix() : "AK";
        String errorCode = errorPrefix+_errorCode;
        if(_exception instanceof AbstractServiceException) {
            AbstractServiceException ase = (AbstractServiceException)_exception;
            ase.setErrorCode(errorCode);
        }
        logException(errorCode,  _exception);
        StandardResponse stdResponse = Utils.createErrorResponse(
                null, errorPrefix, _errorCode, _httpStatus,  _message);
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
    public ResponseEntity<Object> runtimeException(RuntimeException _runEx, WebRequest _request) {
        return createErrorResponse(_runEx, _runEx.getMessage(), "590", null, HttpStatus.INTERNAL_SERVER_ERROR, _request);
    }

    /**
     * Handle Any Exception
     * @param _runEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> throwable(Throwable _runEx, WebRequest _request) {
        return createErrorResponse(_runEx, _runEx.getMessage(), "599", null, HttpStatus.INTERNAL_SERVER_ERROR, _request);
    }

    /**
     * Access Denied Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx, _adEx.getMessage(), "403", null, HttpStatus.FORBIDDEN, _request);
    }

    /**v
     * Exception if the Resource NOT Available!
     * @param _rnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ResourceException.class)
    public ResponseEntity<Object> resourceException(ResourceException _rnfEx,  WebRequest _request) {
        return createErrorResponse(_rnfEx,  "404", _request);
    }

    /**v
     * Exception if the Resource IS NOT FOUND!
     * @param _rnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException _rnfEx,  WebRequest _request) {
        return createErrorResponse(_rnfEx,  "404", _request);
    }

    /**
     * Authorization Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = SecurityException.class)
    public ResponseEntity<Object> securityException(SecurityException _adEx, WebRequest _request) {
        return createErrorResponse(_adEx,  "411",  _request);
    }

    /**
     * Authorization Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<Object> authorizationException(AuthorizationException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx,  "413", _request);
    }

    /**
     * JWT Token Extraction Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = JWTTokenExtractionException.class)
    public ResponseEntity<Object> JWTTokenExtractionException(JWTTokenExtractionException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx, "414",  _request);
    }

    /**
     * JWT Token Expired Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = JWTTokenExpiredException.class)
    public ResponseEntity<Object> JWTTokenExpiredException(JWTTokenExpiredException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx, "415", _request);
    }

    /**
     * JWT Token Subject Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = JWTTokenSubjectException.class)
    public ResponseEntity<Object> JWTTokenSubjectException(JWTTokenSubjectException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx,"416",  _request);
    }

    /**
     * JWT UnDefined Exception
     * @param _adEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = JWTUnDefinedException.class)
    public ResponseEntity<Object> JWTUnDefinedException(JWTUnDefinedException _adEx,  WebRequest _request) {
        return createErrorResponse(_adEx,  "417",  _request);
    }

    /**
     * Messaging Exception
     * @param _msgEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<Object> handleMessagingException(MessagingException _msgEx,  WebRequest _request) {
        return createErrorResponse(_msgEx,  "430", _request);
    }

    /**
     * Database Exception
     * @param _dbEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DatabaseException.class)
    public ResponseEntity<Object> handleDatabaseException(DatabaseException _dbEx,  WebRequest _request) {
        return createErrorResponse(_dbEx,  "440", _request);
    }

    /**
     * Data Not Found Exception
     * @param _dnfEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException _dnfEx,  WebRequest _request) {
        return createErrorResponse(_dnfEx,  "441", _request);
    }

    /**
     * Duplicate Data Exception
     * @param _ddEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = DuplicateDataException.class)
    public ResponseEntity<Object> handleDuplicateDataException(DuplicateDataException _ddEx,  WebRequest _request) {
        return createErrorResponse(_ddEx,  "442", _request);
    }

    /**
     * Unable to Save Exception
     * @param _utEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = UnableToSaveException.class)
    public ResponseEntity<Object> handleUnableToSaveException(UnableToSaveException _utEx,  WebRequest _request) {
        return createErrorResponse(_utEx,  "443", _request);
    }

    /**
     * Business Exception
     * @param _buEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = BusinessServiceException.class)
    public ResponseEntity<Object> handleBusinessServiceException(BusinessServiceException _buEx, WebRequest _request) {
        return createErrorResponse(_buEx,  "460", _request);
    }

    /**
     * InputDataException
     * @param _idEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = InputDataException.class)
    public ResponseEntity<Object> handleInputDataException(InputDataException _idEx,  WebRequest _request) {
        return createErrorResponse(_idEx, "461", _request);
    }

    /**
     * Mandatory Data Required Exception
     * @param _mdrEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = MandatoryDataRequiredException.class)
    public ResponseEntity<Object> handleMandatoryDataRequiredException(MandatoryDataRequiredException _mdrEx,  WebRequest _request) {
        return createErrorResponse(_mdrEx,  "462", _request);
    }

    /**
     * Controller Exception
     * @param _coEx
     * @param _request
     * @return
     */
    @ExceptionHandler(value = ControllerException.class)
    public ResponseEntity<Object> handleControllerException(ControllerException _coEx,  WebRequest _request) {
        return createErrorResponse(_coEx,  "490", _request);
    }

    /**
     *
     * @param _status
     * @param e
     */
    private void logException(String _status, Throwable e) {
        log.info("2|EH|TIME=00|STATUS=Error: {}|CLASS={}|",_status, e.toString());
    }
}
