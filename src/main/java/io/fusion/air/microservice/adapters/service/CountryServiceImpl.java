package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.domain.models.Country;
// import io.fusion.air.microservice.domain.ports.CountryRepository;
import io.fusion.air.microservice.domain.ports.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class CountryServiceImpl implements CountryService {

    // @Autowired
    // CountryRepository countryRepositoryImpl;

    @Override
    public List<Country> getAllCountries() {
        // return (List<Country>) countryRepositoryImpl.findAll();
        return null;
    }
}
