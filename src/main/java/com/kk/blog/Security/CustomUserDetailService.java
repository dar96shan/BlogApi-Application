package com.kk.blog.Security;

import com.kk.blog.entities.User;
import com.kk.blog.exceptions.ResourceNotFoundException;
import com.kk.blog.exceptions.ResourceNotFoundExceptionString;
import com.kk.blog.respositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    public UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundExceptionString("User", "Email", username));
        return user;
    }
}
