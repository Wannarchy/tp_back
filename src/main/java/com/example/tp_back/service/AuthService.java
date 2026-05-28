package com.example.tp_back.service;

import com.example.tp_back.entity.User;
import com.example.tp_back.repository.UserRepository;
import com.example.tp_back.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    public Map<String, String> register(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username déjà utilisé");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(User.Roles.USER); // rôle en dur
        user.setEnabled(true);

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Inscription réussie");
        return response;
    }


    public Map<String, String> login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtUtils.generateToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        return response;
    }
}
