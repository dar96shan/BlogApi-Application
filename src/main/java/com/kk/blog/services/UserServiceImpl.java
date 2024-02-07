package com.kk.blog.services;

import com.kk.blog.entities.Role;
import com.kk.blog.entities.User;
import com.kk.blog.exceptions.ResourceNotFoundException;
import com.kk.blog.payloads.RegisterResponseDto;
import com.kk.blog.payloads.UserDto;
import com.kk.blog.payloads.UserRegisterDto;
import com.kk.blog.respositories.RoleRepo;
import com.kk.blog.respositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kk.blog.config.AppConst.ROLE_NORMAL;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public RegisterResponseDto registerNewUser(UserRegisterDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        //encoded the password
        // Ensure password is not null
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        //roles
        Role role = this.roleRepo.findById(ROLE_NORMAL).get();

        //Assigning new or extra(multiple roles to user)
        user.getRoles().add(role);

        User newUser = this.userRepo.save(user);

        return this.modelMapper.map(newUser,RegisterResponseDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = userRepo.save(user);

        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
       // User user = dtoToUser(userDto);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","id",userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = userRepo.save(user);
        return userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
       User user = userRepo.findById(userId)
               .orElseThrow(()-> new ResourceNotFoundException("user","id",userId));

       return userToDto(user);

    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> getAllUsers = userRepo.findAll();
        return getAllUsers.stream()
                .map(this::userToDto) // Assuming userToDto converts a single User to UserDto
                .collect(Collectors.toList());
    }


    @Override
    public void deleteUser(Integer userId) {
        User deleteUserId = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        userRepo.delete(deleteUserId);
    }

    private User dtoToUser(UserDto userDto){
//        User user = new User();
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setPassword(userDto.getPassword());

        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    private UserDto userToDto(User user){
        UserDto userDto= this.modelMapper.map(user,UserDto.class);
        return userDto;
    }
}
