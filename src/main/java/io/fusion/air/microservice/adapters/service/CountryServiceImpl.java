package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.domain.entities.example.CountryEntity;
import io.fusion.air.microservice.adapters.repository.CountryRepository;
import io.fusion.air.microservice.domain.ports.services.CountryService;
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

    @Autowired
    CountryRepository countryRepositoryImpl;

    @Override
    public List<CountryEntity> getAllCountries() {
        return (List<CountryEntity>) countryRepositoryImpl.findAll();
    }
}
