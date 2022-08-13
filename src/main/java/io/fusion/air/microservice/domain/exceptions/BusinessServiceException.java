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
public class BusinessServiceException extends AbstractServiceException {


    /**
     * Business Service Exception Bad Request
     * @param _msg
     */
    public BusinessServiceException(String _msg) {
        super(_msg);
    }

    /**
     * Business Service Exception Bad Request
     * @param _e
     */
    public BusinessServiceException(Exception _e) {
        super(_e);
    }

    /**
     * Business Service Exception Bad Request
     * @param _msg
     * @param _e
     */
    public BusinessServiceException(String _msg, Exception _e) {
        super(_msg,  _e);
    }

    /**
     * Business Service Exception
     * @param _msg
     * @param badRequest
     * @param _e
     */
    public BusinessServiceException(String _msg, HttpStatus badRequest, Exception _e) {
        super(_msg, badRequest, _e);
    }
}
