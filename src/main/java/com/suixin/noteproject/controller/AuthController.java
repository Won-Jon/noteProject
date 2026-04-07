package com.suixin.noteproject.controller;

import com.suixin.noteproject.dto.LoginRequest;
import com.suixin.noteproject.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final IUserService userService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        log.info("用户注册请求: username={}", request.getUsername());
        userService.register(request.getUsername(), request.getPassword());
        log.info("用户注册成功: username={}", request.getUsername());
        return ResponseEntity.ok().body(Map.of("message", "注册成功"));
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("用户登录请求: username={}", request.getUsername());
        String token = userService.login(request.getUsername(), request.getPassword());
        log.info("用户登录成功: username={}", request.getUsername());
        return ResponseEntity.ok().body(Map.of(
                "token", token,
                "tokenType", "Bearer"
        ));
    }
}