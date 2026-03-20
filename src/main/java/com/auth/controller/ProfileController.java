package com.auth.controller;

import com.auth.dto.ProfileResponse;
import com.auth.dto.UpdateProfileRequest;
import com.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final AuthService authService;

    public ProfileController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ProfileResponse profile(Authentication authentication) {
        return authService.getProfile(authentication.getName());
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return authService.updateProfile(authentication.getName(), request);
    }
}

