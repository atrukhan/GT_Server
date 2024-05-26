package org.example.server.services;

import org.example.server.models.*;
import org.example.server.pojo.CardResponse;
import org.example.server.pojo.LibResponse;
import org.example.server.pojo.MessageResponse;
import org.example.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserLibService {
    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @Autowired
    private DefaultLibraryRepository defaultLibraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    public String generateCode(int libType, long id){
        MessageDigest md = null;
        String res = null;
        String in = String.valueOf(libType).concat(String.valueOf(id));
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(in.getBytes());
            byte[] digest = md.digest();
            res = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public List<LibResponse> getLibsResponse(String userEmail) throws NoSuchElementException{
        User user = userRepository.findByEmail(userEmail).get();
        List<UserLibrary> libs = user.getLibraries();
        List<LibResponse> libsResponse = libs.stream().map(el -> new LibResponse(el.getId(), el.getTitle(), el.getCode(), el.getCards().size())).toList();

        return libsResponse;
    }

    public List<CardResponse> getCardsResponse(String userEmail, Long id) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(id).get();
        if (lib.getUser().getId() == user.getId()) {
            List<CardResponse> cards = lib.getCards().stream().map(el -> new CardResponse(el.getId(), el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample())).toList();
            return cards;
        }else {
            throw new IllegalAccessException("User have not permission");
        }
    }

    public LibResponse createLib(String userEmail, String title) throws NoSuchElementException{
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.save(new UserLibrary(title, "", user));
        lib.setCode(generateCode(1, lib.getId()));
        lib = userLibraryRepository.save(lib);

        Training t1 = new Training(1, 1, false, null, new Date(), lib, null);
        Training t2 = new Training(3, 2, false, null, new Date(), lib, null);
        Training t3 = new Training(7, 3, false, null, new Date(), lib, null);

        List<Training> trainings = trainingRepository.saveAll(List.of(t1,t2,t3));

        return new LibResponse(lib.getId(), lib.getTitle(), lib.getCode(), 0);
    }

    public LibResponse createLibByCode(String userEmail, String title, String code) throws NoSuchElementException {

        User user = userRepository.findByEmail(userEmail).get();

        try {
            UserLibrary lib = userLibraryRepository.findByCode(code).get();

            UserLibrary newLib = userLibraryRepository.save(new UserLibrary(title, "", user));
            newLib.setCode(generateCode(1, newLib.getId()));
            UserLibrary newUserLibrary = userLibraryRepository.save(newLib);

            Training t1 = new Training(1, 1, false, null, new Date(), newUserLibrary, null);
            Training t2 = new Training(3, 2, false, null, new Date(), newUserLibrary, null);
            Training t3 = new Training(7, 3, false, null, new Date(), newUserLibrary, null);

            List<Training> trainings = trainingRepository.saveAll(List.of(t1,t2,t3));

            List<Card> cards = new ArrayList<>();
            lib.getCards().stream().forEach(el -> cards.add((new Card(el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample(), newUserLibrary, null, t1))));

            cardRepository.saveAll(cards);



            return new LibResponse(newUserLibrary.getId(), newUserLibrary.getTitle(), newUserLibrary.getCode(), cards.size());
        }catch (NoSuchElementException e){

        }

        try {
            DefaultLibrary lib = defaultLibraryRepository.findByCode(code).get();

            UserLibrary newLib = userLibraryRepository.save(new UserLibrary(title, "", user));
            newLib.setCode(generateCode(1, newLib.getId()));
            UserLibrary newUserLibrary = userLibraryRepository.save(newLib);

            Training t1 = new Training(1, 1, false, null, new Date(), newUserLibrary, null);
            Training t2 = new Training(3, 2, false, null, new Date(), newUserLibrary, null);
            Training t3 = new Training(7, 3, false, null, new Date(), newUserLibrary, null);

            List<Training> trainings = trainingRepository.saveAll(List.of(t1,t2,t3));

            List<Card> cards = new ArrayList<>();
            lib.getCards().stream().forEach(el -> cards.add((new Card(el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample(), newUserLibrary, null, t1))));

            cardRepository.saveAll(cards);

            return new LibResponse(newUserLibrary.getId(), newUserLibrary.getTitle(), newUserLibrary.getCode(), cards.size());
        }catch (NoSuchElementException e){

        }
        throw new NoSuchElementException("No such library");
    }

    public LibResponse deleteLib(String userEmail, Long id) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(id).get();
        if (lib.getUser().getId() == user.getId()) {
            trainingRepository.deleteAll(lib.getTrainings());
            userLibraryRepository.delete(lib);
            return new LibResponse(lib.getId(), lib.getTitle(), lib.getCode(), lib.getCards().size());
        }else {
            throw new IllegalAccessException("User have not permission");
        }
    }

    public LibResponse updateLib(String userEmail, String title, Long id) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findByEmail(userEmail).get();
        UserLibrary lib = userLibraryRepository.findById(id).get();
        if (lib.getUser().getId() == user.getId()) {
            lib.setTitle(title);
            UserLibrary newLib = userLibraryRepository.save(lib);
            return new LibResponse(newLib.getId(), newLib.getTitle(), newLib.getCode(), newLib.getCards().size());
        }else {
            throw new IllegalAccessException("User have not permission");
        }
    }
}