package com.cropdeal.auth.dto;

import com.cropdeal.auth.modal.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String name;
    private String password;
    private Role role;
}
