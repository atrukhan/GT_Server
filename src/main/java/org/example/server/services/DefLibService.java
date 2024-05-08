package org.example.server.services;

import org.example.server.models.DefaultLibrary;
import org.example.server.models.User;
import org.example.server.pojo.CardResponse;
import org.example.server.pojo.LibResponse;
import org.example.server.pojo.MessageResponse;
import org.example.server.repositories.ConfirmationTokenRepository;
import org.example.server.repositories.DefaultLibraryRepository;
import org.example.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DefLibService {
    @Autowired
    private DefaultLibraryRepository defaultLibraryRepository;
    public List<LibResponse> getLibsResponse(){
        List<DefaultLibrary> libs = defaultLibraryRepository.findAll();
        List<LibResponse> libsResponse = libs.stream().map(el -> new LibResponse(el.getId(), el.getTitle(), el.getCode(), el.getCards().size())).toList();
        return libsResponse;
    }

    public List<CardResponse> getCardsResponse(Long id) throws NoSuchElementException {
        DefaultLibrary lib = defaultLibraryRepository.findById(id).get();
        List<CardResponse> cardsResponse = lib.getCards().stream().map(el -> new CardResponse(el.getId(), el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample())).toList();
        return cardsResponse;
    }
}
