package com.kk.blog.respositories;

import com.kk.blog.entities.User;
import com.kk.blog.payloads.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

}
