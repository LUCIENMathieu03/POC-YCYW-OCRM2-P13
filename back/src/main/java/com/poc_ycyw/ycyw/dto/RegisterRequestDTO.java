package com.poc_ycyw.ycyw.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    String email;
    String password;
    String role;
    String name;
}
