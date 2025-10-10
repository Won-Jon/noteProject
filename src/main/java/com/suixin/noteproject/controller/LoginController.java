package com.suixin.noteproject.controller;

import com.suixin.noteproject.dto.LoginRequest;
import com.suixin.noteproject.entity.User;
import com.suixin.noteproject.service.IUserService;
import com.suixin.noteproject.util.JwtUtil;
import com.suixin.noteproject.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userService.findByUsername(req.getUsername());
        if (user != null && passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername()));
        }
        return ResponseEntity.status(401).body("用户名或密码错误");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(400).body("用户名已存在");
        }
        user.setPassword(user.getPassword());
        userService.save(user);
        return ResponseEntity.ok("注册成功");
    }
}
