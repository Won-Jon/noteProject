package com.suixin.noteproject.controller;

import com.suixin.noteproject.dto.LoginRequest;
import com.suixin.noteproject.service.IAuthService;
import com.suixin.noteproject.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        userService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok().body(Map.of("message", "注册成功"));
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok().body(Map.of(
                "token", token,
                "tokenType", "Bearer"
        ));
    }
}