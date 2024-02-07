package com.kk.blog.controllers;

import com.kk.blog.Security.JwtTokenHelper;
import com.kk.blog.entities.User;
import com.kk.blog.exceptions.ApiException;
import com.kk.blog.payloads.*;
import com.kk.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request) throws Exception {
        this.authenticate(request.getUsername(),request.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username,password);
        try {
            this.authenticationManager.authenticate(authToken);
        }catch (BadCredentialsException e){
//            System.out.println(e.getMessage());
//            System.out.println("Invalid Details..!!!");
            throw new ApiException("Invalid Username or Password");
        }


    }

    //Register new USer api
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody UserRegisterDto userDto){
        RegisterResponseDto registerResponseDto = this.userService.registerNewUser(userDto);
        return new ResponseEntity<>(registerResponseDto,HttpStatus.CREATED);

    }
}
