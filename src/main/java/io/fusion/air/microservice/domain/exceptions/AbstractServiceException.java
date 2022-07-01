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
package io.fusion.air.microservice.domain.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public abstract class AbstractServiceException extends  RuntimeException {

    private final String errorMessage;
    private final Exception serviceException;
    private HttpStatus httpStatus;

    /**
     * Abstract Service Exception
     * @param _e
     */
    public AbstractServiceException(Exception _e) {
        super(_e);
        errorMessage = (_e != null) ? _e.getMessage() : "No-Info Available" ;
        serviceException = _e;
        httpStatus = HttpStatus.NOT_FOUND;
    }

    /**
     * Service Base Exception
     * @param _msg
     * @param _e
     */
    public AbstractServiceException(String _msg, Exception _e) {
        super(_msg, _e);
        errorMessage = (_msg != null) ? _msg : "No-Info Available" ;
        serviceException = _e;
        httpStatus = HttpStatus.NOT_FOUND;
    }

    /**
     * Service base Exception
     * @param _msg
     * @param _status
     * @param _e
     */
    public AbstractServiceException(String _msg, HttpStatus _status, Exception _e) {
        super(_msg, _e);
        errorMessage = (_msg != null) ? _msg : "No-Info Available" ;
        serviceException = _e;
        httpStatus = _status;
    }

    /**
     * Get the App Error Message
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the Exception
     * @return
     */
    public Exception getServiceException() {
        return serviceException;
    }

    /**
     * Returns HttpStatus
     * @return
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}