package org.example.server.controllers;

import org.example.server.models.Card;
import org.example.server.models.DefaultLibrary;
import org.example.server.models.UserLibrary;
import org.example.server.models.User;
import org.example.server.pojo.*;
import org.example.server.repositories.CardRepository;
import org.example.server.repositories.DefaultLibraryRepository;
import org.example.server.repositories.UserLibraryRepository;
import org.example.server.repositories.UserRepository;
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
