package com.kk.blog.payloads;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RoleDto {

    private int id;
    private String name;
}
