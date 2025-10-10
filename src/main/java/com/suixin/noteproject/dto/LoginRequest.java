package com.suixin.noteproject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequest {

    private String username;

    private String password;
}
