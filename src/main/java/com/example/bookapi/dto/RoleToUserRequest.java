package com.example.bookapi.dto;

import com.example.bookapi.enums.RoleType;
import lombok.Data;

@Data
public class RoleToUserRequest {
    private String username;
    private RoleType roleName;
}
