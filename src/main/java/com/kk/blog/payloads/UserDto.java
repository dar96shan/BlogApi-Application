package com.kk.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kk.blog.entities.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotEmpty
    @Size(min=4,message = "username must be min of 4 characters")
    private String name;

    @NotEmpty
    @Email(message = "your email is not valid")
    private String email;

    @NotEmpty
    @Size(min = 3,max = 10,message = "Password must be min of 3 characters and max of 10 characters!")
    private String password;

    @NotEmpty
    private String about;

    private Set<RoleDto> roles = new HashSet<>();

}
