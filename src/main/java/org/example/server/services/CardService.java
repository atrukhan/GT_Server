package org.example.server.services;

import org.example.server.models.Card;
import org.example.server.models.User;
import org.example.server.models.UserLibrary;
import org.example.server.pojo.CardCreateRequest;
import org.example.server.pojo.CardResponse;
import org.example.server.pojo.LibResponse;
import org.example.server.pojo.MessageResponse;
import org.example.server.repositories.CardRepository;
import org.example.server.repositories.UserLibraryRepository;
import org.example.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    public CardResponse createCard(String userEmail, CardCreateRequest customReq, Long id) throws NoSuchElementException, IllegalAccessException{

        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(id).get();
        if (lib.getUser().getId() == user.getId()) {
            Card c = cardRepository.save(new Card(customReq.getValue(), customReq.getTranscription(), customReq.getTranslation(), customReq.getExample(), lib, null, null));
            return new CardResponse(c.getId(), c.getValue(), c.getTranscription(), c.getTranslation(), c.getExample());
        }else {
            throw new IllegalAccessException("User have not permission");
        }

    }

    public CardResponse deleteCard(String userEmail, Long libIid, Long cardId) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(libIid).get();
        if (lib.getUser().getId() == user.getId()) {
            List<Card> filterCards = lib.getCards().stream().filter(el -> el.getId() == cardId).collect(Collectors.toList());
            if (filterCards.size() > 0){
                Card card = filterCards.get(0);
                cardRepository.delete(card);
                return new CardResponse(card.getId(), card.getValue(), card.getTranscription(), card.getTranslation(), card.getExample());
            }else {
                throw new NoSuchElementException("No such card");
            }
        }else {
            throw new IllegalAccessException("User have not permission");
        }
    }

    public CardResponse updateLib(String userEmail, CardCreateRequest customReq, Long libIid, Long cardId) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(libIid).get();
        if (lib.getUser().getId() == user.getId()) {
            List<Card> filterCards = lib.getCards().stream().filter(el -> el.getId() == cardId).collect(Collectors.toList());
            if (filterCards.size() > 0){
                Card card = filterCards.get(0);
                card.setValue(customReq.getValue());
                card.setTranscription(customReq.getTranscription());
                card.setTranslation(customReq.getTranslation());
                card.setExample(customReq.getExample());
                Card newCard = cardRepository.save(card);
                return new CardResponse(newCard.getId(), newCard.getValue(), newCard.getTranscription(), newCard.getTranslation(), newCard.getExample());
            }else {
                throw new NoSuchElementException("No such card");
            }
        }else {
            throw new IllegalAccessException("User have not permission");
        }
    }
}
