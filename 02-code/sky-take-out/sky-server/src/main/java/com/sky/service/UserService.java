package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @Author liming
 * @Date 2023/11/2 8:55
 * @Description
 **/


public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
