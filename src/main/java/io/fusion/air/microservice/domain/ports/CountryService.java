package io.fusion.air.microservice.domain.ports;

import io.fusion.air.microservice.domain.models.Country;
import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface CountryService {

    /**
     * Returns all the Countryy
     * @return
     */
    public List<Country> getAllCountries();
}
