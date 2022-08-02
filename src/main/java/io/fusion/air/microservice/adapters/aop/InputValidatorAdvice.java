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
import javax.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@ControllerAdvice
@Order(1)
public class InputValidatorAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handling Invalid Input in Requests
     * @param _manvEx
     * @param _headers
     * @param _status
     * @param _request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException _manvEx,
                                      HttpHeaders _headers, HttpStatus _status, WebRequest _request) {

        StandardResponse stdResponse = new StandardResponse();
        stdResponse.init(false, "400", "Input Errors");
        List<String> errors = new ArrayList<String>();
        _manvEx.getBindingResult().getAllErrors().forEach((error) -> {
            try {
                errors.add(((FieldError) error).getField() + "|" + error.getDefaultMessage());
            } catch (Exception ignored) {}
        });
        Collections.sort(errors);
        stdResponse.setPayload(errors);
        return new ResponseEntity<>(stdResponse, _headers, HttpStatus.BAD_REQUEST);
    }
}
