package io.fusion.air.microservice.adapters.repository;

import io.fusion.air.microservice.domain.entities.example.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
