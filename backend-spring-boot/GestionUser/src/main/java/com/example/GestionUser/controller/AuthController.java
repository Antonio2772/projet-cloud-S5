package com.example.GestionUser.controller;

import com.example.GestionUser.model.ConfirmationToken;
import com.example.GestionUser.model.User;
import com.example.GestionUser.service.UserService;
import com.example.GestionUser.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user.getNom(), user.getPrenom(), user.getEmail(), user.getPassword());
        return "Utilisateur inscrit, veuillez vérifier votre email pour confirmation.";
    }

    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam("token") String token) {
        Optional<ConfirmationToken> confirmationTokenOpt = confirmationTokenRepository.findByToken(token);
        if (confirmationTokenOpt.isEmpty()) {
            return "Token invalide ou expiré.";
        }

        ConfirmationToken confirmationToken = confirmationTokenOpt.get();
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userService.save(user);
        confirmationTokenRepository.delete(confirmationToken);

        return "Votre email a été confirmé avec succès !";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isEnabled()) {
                return "Veuillez d'abord confirmer votre email.";
            }

            if (password.equals(user.getPassword())) {
                userService.generateAndSendPin(user);
                return "Un PIN a été envoyé. Veuillez le valider.";
            }
        }

        return "Email ou mot de passe invalide.";
    }

    @PostMapping("/validate-pin")
    public String validatePin(@RequestParam("email") String email, @RequestParam("pin") String pin) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (userService.validatePin(user, pin)) {
                return "PIN validé avec succès ! Vous êtes maintenant connecté.";
            } else {
                return "PIN invalide ou expiré. Veuillez réessayer.";
            }
        }

        return "Utilisateur non trouvé.";
    }

    @PutMapping("/editUser")
    public String editUser(@RequestBody User updatedUser) {
        Optional<User> userOpt = userService.findByEmail(updatedUser.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setNom(updatedUser.getNom());
            user.setPrenom(updatedUser.getPrenom());
            user.setPassword(updatedUser.getPassword());
            userService.save(user);

            return "Informations mises à jour avec succès.";
        }

        return "Utilisateur non trouvé.";
    }
}
