package org.example.server.controllers;

import org.example.server.config.jwt.JwtUtils;
import org.example.server.models.EntryDate;
import org.example.server.models.enums.ERole;
import org.example.server.models.Role;
import org.example.server.models.RefreshToken;
import org.example.server.models.User;
import org.example.server.pojo.JwtResponse;
import org.example.server.pojo.LoginRequest;
import org.example.server.pojo.MessageResponse;
import org.example.server.pojo.SignupRequest;
import org.example.server.repositories.EntryDateRepository;
import org.example.server.repositories.RoleRepository;
import org.example.server.repositories.RefreshTokenRepository;
import org.example.server.repositories.UserRepository;
import org.example.server.services.UserService;
import org.example.server.services.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EntryDateRepository entryDateRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest req, HttpServletResponse response){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userDetails.getId()));

        EntryDate entryDate = new EntryDate(new Date(), user);
        entryDateRepository.save(entryDate);

        if(!user.getActivated()){
            return ResponseEntity.badRequest()
                    .body(new JwtResponse(
                            null,
                            userDetails.getId(),
                            userDetails.getNickname(),
                            userDetails.getEmail(),
                            roles,
                            "Error: Couldn't verify email"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtAccess = jwtUtils.generateJwtAccessToken(authentication);
        String jwtRefresh = jwtUtils.generateJwtRefreshToken(authentication);


//        Cookie cookie = new Cookie("jwt", jwtRefresh);
//        cookie.setMaxAge(jwtUtils.getJwtRefreshExpirationMs() / 1000);
        Cookie cookie = new Cookie("jwt", jwtAccess);
        cookie.setMaxAge(jwtUtils.getJwtAccessExpirationMs() / 1000);
        cookie.setPath("/api/user/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

//        RefreshToken token = new RefreshToken(jwtRefresh, user);
//        tokenRepository.save(token);

        response.addCookie(cookie);

//        return ResponseEntity.ok(new JwtResponse(jwtAccess,
        return ResponseEntity.ok(new JwtResponse(jwtRefresh,
                userDetails.getId(),
                userDetails.getNickname(),
                userDetails.getEmail(),
                roles,
                null));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest req){

        if(userRepository.existsByEmail(req.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is exist"));
        }

        User user = new User(req.getNickname(), req.getEmail(), passwordEncoder.encode(req.getPassword()), false, null);

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository
                .findByValue(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));

        roles.add(userRole);


        user.setRoles(roles);

        return userService.saveUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("jwt", " ");
        cookie.setMaxAge(1);
        cookie.setPath("/api/user/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseEntity.ok(new MessageResponse("Logout"));
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }
}
