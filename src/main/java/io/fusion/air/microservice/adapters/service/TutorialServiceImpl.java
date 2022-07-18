package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.domain.entities.Tutorial;
import io.fusion.air.microservice.domain.ports.TutorialRepository;
import io.fusion.air.microservice.domain.ports.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    TutorialRepository tutorialRepositoryImpl;

    @Override
    public List<Tutorial> getAllTutorials() {
        return tutorialRepositoryImpl.findAll();
    }
}
