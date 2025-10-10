package com.suixin.noteproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suixin.noteproject.entity.User;

import java.util.Optional;

public interface IUserService extends IService<User> {

    void register(String username, String rawPassword);

    Optional<User> findByUsername(String username);

}
