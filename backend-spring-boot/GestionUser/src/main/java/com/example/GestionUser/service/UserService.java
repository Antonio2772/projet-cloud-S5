package com.example.GestionUser.service;

import com.example.GestionUser.model.User;
import com.example.GestionUser.model.ConfirmationToken;
import com.example.GestionUser.repository.ConfirmationTokenRepository;
import com.example.GestionUser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public UserService(UserRepository userRepository, EmailService emailService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void registerUser(String nom, String prenom, String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        
        User user = new User(null, nom, prenom, email, password, false, null, null);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, user);
        confirmationTokenRepository.save(confirmationToken);

        sendConfirmationEmail(user, token);
    }

    private void sendConfirmationEmail(User user, String token) {
        String confirmationUrl = "http://localhost:8080/confirm-email?token=" + token;
        String subject = "Confirmez votre inscription";
        String body = "Cliquez sur le lien suivant pour confirmer votre inscription : " + confirmationUrl;
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void generateAndSendPin(User user) {
        String pin = generatePin();
        user.setPinCode(pin);
        user.setPinExpiration(LocalDateTime.now().plusSeconds(90));
        userRepository.save(user);

        String subject = "Votre code PIN de connexion";
        String body = "Votre code PIN est : " + pin + ". Il expirera dans 90 secondes.";
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String generatePin() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  
    }

    public boolean validatePin(User user, String pin) {
        if (user.getPinExpiration() != null && user.getPinExpiration().isBefore(LocalDateTime.now())) {
            return false;  
        }
        return user.getPinCode() != null && user.getPinCode().equals(pin);
    }

    public void save(User user) {
        userRepository.save(user);  
    }
}
