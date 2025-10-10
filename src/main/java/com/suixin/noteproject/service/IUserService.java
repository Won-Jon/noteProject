package com.suixin.noteproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suixin.noteproject.entity.User;

public interface IUserService extends IService<User> {

    User findByUsername(String username);

}
