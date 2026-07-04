package com.suzumiya.campustrade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/* 为了不污染 User 实体（因为登录只需要 username 和 password，而不需要校验 phone 等字段） */
@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
