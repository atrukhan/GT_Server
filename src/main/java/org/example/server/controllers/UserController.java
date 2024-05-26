package org.example.server.controllers;

import org.example.server.models.*;
import org.example.server.pojo.*;
import org.example.server.repositories.*;
import org.example.server.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DefLibService defLibService;

    @Autowired
    private UserLibService userLibService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private EntryDateRepository entryDateRepository;
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private CardService cardService;
    private final String TRANSLATE_EN_URL = "https://ftapi.pythonanywhere.com/translate?sl=en&dl=ru&text=";
    private final String TRANSLATE_RU_URL = "https://ftapi.pythonanywhere.com/translate?sl=ru&dl=en&text=";



    @PostMapping("/check_auth")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public  ResponseEntity<?>  checkAuth(HttpServletRequest req){
        User user = userRepository.findByEmail((String) req.getAttribute("email")).get();
        return ResponseEntity.ok(new JwtResponse(null,
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                null,
                null));
    }

    @PostMapping("/def_libs")
    public  ResponseEntity<?>  getDefaultLibraries(){
        return ResponseEntity.ok(defLibService.getLibsResponse());
    }

    @PostMapping("/def_lib_cards/{id}")
    public  ResponseEntity<?>  getDefaultLibraryCards(@PathVariable("id") Long id){
        try {
            List<CardResponse> cards = defLibService.getCardsResponse(id);
            return ResponseEntity.ok(cards);
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Bad request"));
        }
    }

    @PostMapping("/user_libs")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getUserLibraries( HttpServletRequest req) {
        try {
            return ResponseEntity.ok(userLibService.getLibsResponse((String) req.getAttribute("email")));
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Bad request"));
        }
    }

    @PostMapping("/user_lib_cards/{id}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getUserLibraryCards(@PathVariable("id") Long id, HttpServletRequest req){
        try{
            return ResponseEntity.ok(userLibService.getCardsResponse((String) req.getAttribute("email"), id));
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Bad request"));
        } catch (IllegalAccessException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

    @PostMapping("/user_lib_create")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  createUserLibrary(@RequestBody LibCreateRequest customReq, HttpServletRequest req){
        try{
            return ResponseEntity.ok(userLibService.createLib((String) req.getAttribute("email"), customReq.getTitle()));
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Bad request"));
        }
    }

    @PostMapping("/user_lib_create/{code}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  createUserLibrary(@PathVariable("code") String code, @RequestBody LibCreateRequest customReq, HttpServletRequest req){
        try{
            return ResponseEntity.ok(userLibService.createLibByCode((String) req.getAttribute("email"), customReq.getTitle(), code));
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        }
    }

    @PostMapping("/user_lib_delete/{libId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  deleteUserLibrary(@PathVariable("libId") Long libId, HttpServletRequest req){
        try{
            return ResponseEntity.ok(userLibService.deleteLib((String) req.getAttribute("email"), libId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


    @PostMapping("/user_lib_update/{libId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  updateUserLibrary(@PathVariable("libId") Long libId,  @RequestBody LibCreateRequest customReq, HttpServletRequest req){
        try{
            return ResponseEntity.ok(userLibService.updateLib((String) req.getAttribute("email"), customReq.getTitle(), libId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/user_lib_card_create/{libId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  createUserLibraryCard(@PathVariable("libId") Long libId, @RequestBody CardCreateRequest customReq, HttpServletRequest req){
        try {
            return ResponseEntity.ok(cardService.createCard((String) req.getAttribute("email"), customReq, libId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

    @PostMapping("/user_lib_card_update/{libId}/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  updateUserLibraryCard(@PathVariable("libId") Long libId, @PathVariable("cardId") Long cardId, @RequestBody CardCreateRequest customReq, HttpServletRequest req){
        try{
            return ResponseEntity.ok(cardService.updateCard((String) req.getAttribute("email"), customReq, libId, cardId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/user_lib_card_delete/{libId}/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  deleteUserLibrary(@PathVariable("libId") Long libId, @PathVariable("cardId") Long cardId, HttpServletRequest req){
        try{
            return ResponseEntity.ok(cardService.deleteCard((String) req.getAttribute("email"), libId, cardId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/trainings/{libId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getTrainings(@PathVariable("libId") Long libId, HttpServletRequest req){
        try {
            return ResponseEntity.ok(trainingService.getTrainings((String) req.getAttribute("email"), libId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

    @GetMapping("/training_cards/{trainingId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getTrainingCards(@PathVariable("trainingId") Long trainingId, HttpServletRequest req){
        try {
            return ResponseEntity.ok(trainingService.getTrainingCards((String) req.getAttribute("email"), trainingId));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

    @PostMapping("/move_card/{trainingId}/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  moveTrainingCard(@PathVariable("trainingId") Long trainingId, @PathVariable("cardId") Long cardId, @RequestBody MoveCardRequest customReq, HttpServletRequest req){
        try {
            trainingService.moveTrainingCard((String) req.getAttribute("email"), trainingId, cardId, customReq.getUp());
            return ResponseEntity.ok(null);
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

    @GetMapping("/entry_dates")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getEntryDates(HttpServletRequest req){
        try {
            User user = userRepository.findByEmail((String) req.getAttribute("email")).get();
            List<EntryDateResponse> dates = entryDateRepository.findByUser(user).stream().map(el -> new EntryDateResponse(el.getId(), el.getDate())).toList();

            return ResponseEntity.ok(dates);
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such library"));
        }

    }

    @PostMapping("/test_cards")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  getTestCards( @RequestBody TestCardsRequest customReq, HttpServletRequest req){
        try {
            User user = userRepository.findByEmail((String) req.getAttribute("email")).get();
            List<Card> cards = new ArrayList<>();
            List<UserLibrary> libs = userLibraryRepository.findAllById(customReq.getIds());
            for (UserLibrary lib : libs){
                if(user.getId() == lib.getUser().getId())
                    cards.addAll(lib.getCards());
            }
            List<CardResponse> cardsResponse = cards.stream().map(el -> new CardResponse(el.getId(), el.getValue(), el.getTranscription(), el.getTranslation(), el.getExample())).toList();

            return ResponseEntity.ok(cardsResponse);
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such element"));
        }

    }

    @PostMapping("/save_test")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?>  saveTestResult( @RequestBody SaveTestResultRequest customReq, HttpServletRequest req){
        try {
            User user = userRepository.findByEmail((String) req.getAttribute("email")).get();
            List<UserLibrary> libs = userLibraryRepository.findAllById(customReq.getIds());
            testRepository.save(new Test(customReq.getMistakesCount(), customReq.getCardsCount(), true, new Date(), user, libs));
            return ResponseEntity.ok(null);
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No such element"));
        }

    }

    @GetMapping("/translate/en/{value}")
    public  ResponseEntity<?>  translateEng(@PathVariable("value") String value, HttpServletRequest req){
        try {
            String url = TRANSLATE_EN_URL + value;
            RestTemplate restTemplate = new RestTemplate();
            Object res = restTemplate.getForObject(url, Object.class);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/translate/ru/{value}")
    public  ResponseEntity<?>  translateRus(@PathVariable("value") String value, HttpServletRequest req){
        try {
            String url = TRANSLATE_RU_URL + value;
            RestTemplate restTemplate = new RestTemplate();
            Object res = restTemplate.getForObject(url, Object.class);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
