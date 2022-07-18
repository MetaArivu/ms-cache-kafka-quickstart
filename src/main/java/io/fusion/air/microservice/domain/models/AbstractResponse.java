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
package io.fusion.air.microservice.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public abstract class AbstractResponse implements Serializable {

    @JsonProperty("rs")
    private boolean success = false;
    @JsonProperty("rc")
    private String code = "ERROR";
    @JsonProperty("rd")
    private String description = "Default Error Message!";

    @JsonProperty("payload")
    private Object payload;

    /**
     * Set the Response Status, Code and Description
     * @param _status
     * @param _code
     * @param _desc
     */
    public void init(boolean _status, String _code, String _desc) {
        success = _status;
        code = _code;
        description = _desc;
    }

    /**
     * Set the Payload
     * @param payload
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * Returns True if the Response is Success else False
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Returns the Error Code
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the Description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retuurns the Payload
     * @return
     */
    public Object getPayload() {
        return payload;
    }
}