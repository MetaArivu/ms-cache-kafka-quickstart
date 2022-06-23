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
