package org.example.server.services;

import org.example.server.models.Card;
import org.example.server.models.Training;
import org.example.server.models.User;
import org.example.server.models.UserLibrary;
import org.example.server.pojo.CardCreateRequest;
import org.example.server.pojo.CardResponse;
import org.example.server.pojo.TrainingInfoResponse;

import org.example.server.repositories.TrainingRepository;
import org.example.server.repositories.UserLibraryRepository;
import org.example.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLibraryRepository userLibraryRepository;


    public List<TrainingInfoResponse> getTrainings(String userEmail, Long libId) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(libId).get();
        if (lib.getUser().getId() == user.getId()) {
            lib.getTrainings().stream().filter(el -> el.getAvailable() == false && el.getNextDate().compareTo(new Date()) <= 0).toList();
            List<TrainingInfoResponse> trainings = lib.getTrainings().stream().map(el -> new TrainingInfoResponse(el.getId(), el.getPeriod(), el.getLevel(), el.getAvailable(), el.getPrevDate(), el.getNextDate())).toList();
            return trainings;
        } else {
            throw new IllegalAccessException("User have not permission");
        }
    }

    public List<CardResponse> getTrainingCards(String userEmail, Long trainingId) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        Training training = trainingRepository.findById(trainingId).get();
        if (training.getLibrary().getUser().getId() == user.getId()) {
            List<CardResponse> cardResponses = training.getCards().stream().map(el -> new CardResponse(el.getId(), el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample())).toList();
            return cardResponses;
        } else {
            throw new IllegalAccessException("User have not permission");
        }
    }
}
