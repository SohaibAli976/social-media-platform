package com.social_media_platform.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class LoginRequest {

    private String email;
    private String password;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

