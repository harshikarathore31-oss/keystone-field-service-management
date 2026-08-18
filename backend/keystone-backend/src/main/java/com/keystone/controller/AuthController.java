package com.keystone.controller;

import com.keystone.dto.AuthResponse;
import com.keystone.dto.LoginRequest;
import com.keystone.dto.RegisterRequest;
import com.keystone.entity.Role;
import com.keystone.entity.User;
import com.keystone.repository.UserRepository;
import com.keystone.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(
            AuthService authService,
            UserRepository userRepository) {

        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        System.out.println("========== LOGIN API HIT ==========");

        return authService.login(request);
    }

    @GetMapping("/technicians")
    public List<User> getTechnicians() {

        return userRepository.findByRole(Role.TECHNICIAN);
    }
}