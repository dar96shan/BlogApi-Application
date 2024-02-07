package com.kk.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class RegisterResponseDto {

    private Integer id;
    private String name;

    private String email;
    @JsonIgnore
    private String password;

    private String about;

    private Set<RoleDto> roles = new HashSet<>();
}
