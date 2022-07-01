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

import java.io.Serializable;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class Country implements Serializable {

    private String countryId;
    private String countryName;
    private String countryOfficialName;
    private String countryCode;

    /**
     * Create Country
     * @param _pid
     * @param _countryNm
     * @param _countryOnm
     * @param _countryCd
     */
    public Country(String _pid, String _countryNm, String _countryOnm, String _countryCd)  {
        countryId   = _pid;
        countryName = _countryNm;
        countryOfficialName = _countryOnm;
        countryCode   = _countryCd;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryOfficialName() {
        return countryOfficialName;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
