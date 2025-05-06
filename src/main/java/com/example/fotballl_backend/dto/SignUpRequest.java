package com.example.fotballl_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String username;
    private String email;
    private String password;

}
