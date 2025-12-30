package com.hfut.studyroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求
 */
@Data
public class LoginRequest {
    
    @NotBlank(message = "学号不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
}
