package com.kk.blog.services;

import com.kk.blog.payloads.RegisterResponseDto;
import com.kk.blog.payloads.UserDto;
import com.kk.blog.payloads.UserRegisterDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    RegisterResponseDto registerNewUser(UserRegisterDto userDto);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,Integer userId);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    void deleteUser(Integer userId);
}
