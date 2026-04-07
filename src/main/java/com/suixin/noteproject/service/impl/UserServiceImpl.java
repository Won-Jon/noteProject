package com.suixin.noteproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suixin.noteproject.entity.User;
import com.suixin.noteproject.service.IUserService;
import com.suixin.noteproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // ← 构造器注入
public class UserServiceImpl extends ServiceImpl<BaseMapper<User>, User> implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder; // ← 由 SecurityConfig 提供

    @Override
    public void register(String username, String rawPassword) {
        log.info("开始注册用户: username={}", username);
        long name = this.count(new QueryWrapper<User>().eq("username", username));
        if (name > 0) {
            log.warn("用户注册失败，用户名已存在: username={}", username);
            throw new IllegalArgumentException("用户名已存在");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        // 可扩展：设置默认角色、邮箱等
        this.save(user);
        log.info("用户注册成功: username={}, userId={}", username, user.getId());
    }

    /**
     * 登录：验证凭据，生成 Token
     */
    @Override
    public String login(String username, String rawPassword) {
        log.debug("尝试登录: username={}", username);
        User user = this.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("登录失败，用户不存在: username={}", username);
                    return new BadCredentialsException("用户名或密码错误");
                });

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.warn("登录失败，密码错误: username={}", username);
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 生成 Token（包含 userId 和 roles）
        String token = JwtUtil.generateToken(user);
        log.info("用户登录成功: username={}, userId={}", username, user.getId());
        return token;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<User>().eq("username", username)));
    }
}
