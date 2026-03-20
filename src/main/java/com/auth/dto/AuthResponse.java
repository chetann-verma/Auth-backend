package com.auth.dto;

public class AuthResponse {

    private String token;
    private String tokenType;
    private String email;
    private String name;

    public AuthResponse(String token, String email, String name) {
        this.token = token;
        this.tokenType = "Bearer";
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}

