package io.fusion.air.microservice.domain.ports;

import io.fusion.air.microservice.domain.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
