package com.suixin.noteproject.util;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
