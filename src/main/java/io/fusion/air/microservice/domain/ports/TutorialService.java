package io.fusion.air.microservice.domain.ports;

import io.fusion.air.microservice.domain.models.Country;
import io.fusion.air.microservice.domain.models.Tutorial;

import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface TutorialService {

    /**
     * Returns all the Countryy
     * @return
     */
    public List<Tutorial> getAllTutorials();
}
