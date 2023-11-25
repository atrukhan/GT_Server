package org.example.server.services;

import org.example.server.models.ConfirmationToken;
import org.example.server.models.User;
import org.example.server.pojo.MessageResponse;
import org.example.server.repositories.ConfirmationTokenRepository;
import org.example.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    EmailService emailService;

    public List<User> getUsers(){
       return null;
    }

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findById(token.getUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + token.getUser().getId()));
            user.setEnabled(true);
            userRepository.save(user);
            confirmationTokenRepository.delete(token);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

    public ResponseEntity<?> saveUser(User user) {

        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/api/auth/confirm-account?token="+confirmationToken.getToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getToken());

        return ResponseEntity.ok(new MessageResponse("Verify email by the link sent on your email address"));
    }
}
