package com.example.tp_back.service;

import com.example.tp_back.entity.User;
import com.example.tp_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Liste tous les utilisateurs (ADMIN)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Récupérer un utilisateur par id
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + id));
    }

    // Modifier les infos d'un utilisateur (ADMIN)
    public User updateUser(Integer id, User updated) {
        User user = getUserById(id);

        if (updated.getUsername() != null) user.setUsername(updated.getUsername());
        if (updated.getEmail() != null)    user.setEmail(updated.getEmail());
        if (updated.getRoles() != null)    user.setRoles(updated.getRoles());
        user.setEnabled(updated.isEnabled());

        // Si un nouveau mot de passe est fourni, on le encode
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        return userRepository.save(user);
    }

    // Supprimer un utilisateur (ADMIN)
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé : " + id);
        }
        userRepository.deleteById(id);
    }
}
