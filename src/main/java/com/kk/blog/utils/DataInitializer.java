package com.kk.blog.utils;

import com.kk.blog.config.AppConst;
import com.kk.blog.entities.Role;
import com.kk.blog.entities.User;
import com.kk.blog.respositories.RoleRepo;
import com.kk.blog.respositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.passwordEncoder.encode("123"));

        try {
            Role role = new Role();
            role.setId(AppConst.ROLE_ADMIN);
            role.setName("ROLE_ADMIN");

            Role role1 = new Role();
            role1.setId(AppConst.ROLE_NORMAL);
            role1.setName("ROLE_NORMAL");

            List<Role> roles = List.of(role, role1);

            List<Role> result = roleRepo.saveAll(roles);
            result.forEach(r-> System.out.println(r.getName()));
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}

