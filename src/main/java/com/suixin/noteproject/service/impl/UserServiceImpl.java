package com.suixin.noteproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suixin.noteproject.entity.User;
import com.suixin.noteproject.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // ← 构造器注入
public class UserServiceImpl extends ServiceImpl<BaseMapper<User>, User> implements IUserService {

    private final PasswordEncoder passwordEncoder; // ← 由 SecurityConfig 提供

    @Override
    public void register(String username, String rawPassword) {
        long name = this.count(new QueryWrapper<User>().eq("username", username));
        if (name > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        // 可扩展：设置默认角色、邮箱等
        this.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<User>().eq("username", username)));
    }
}
