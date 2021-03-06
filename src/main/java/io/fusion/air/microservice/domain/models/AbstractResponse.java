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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fusion.air.microservice.utils.DateJsonSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@JsonPropertyOrder({  "rt", "rs", "rc" , "rd", "payload"})
public abstract class AbstractResponse implements Serializable {

    @JsonProperty("rt")
    @JsonSerialize(using = DateJsonSerializer.class)
    private LocalDateTime requestTime = LocalDateTime.now();

    @JsonProperty("rs")
    private boolean success = false;
    @JsonProperty("rc")
    private String code = "ERROR";
    @JsonProperty("rd")
    private String description = "Default Error Message!";

   @JsonProperty("payload")
    private Object payload = null;

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
     * @param _payload
     */
    public void setPayload(Object _payload) {
        this.payload = _payload;
    }

    /**
     * Request Time
     * @return
     */
    public LocalDateTime getRequestTime() {
        return requestTime;
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