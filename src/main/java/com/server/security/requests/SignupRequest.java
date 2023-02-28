package com.server.security.requests;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Set<String> role;

    public SignupRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
