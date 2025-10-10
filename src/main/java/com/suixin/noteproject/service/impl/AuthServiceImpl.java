package com.suixin.noteproject.service.impl;

import com.suixin.noteproject.entity.User;
import com.suixin.noteproject.service.IAuthService;
import com.suixin.noteproject.service.IUserService;
import com.suixin.noteproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录：验证凭据，生成 Token
     */
    @Override
    public String login(String username, String rawPassword) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 生成 Token（包含 userId 和 roles）
        return jwtUtil.generateToken(user);
    }
}