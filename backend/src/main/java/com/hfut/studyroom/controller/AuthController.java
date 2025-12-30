package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.LoginRequest;
import com.hfut.studyroom.dto.LoginResponse;
import com.hfut.studyroom.dto.RegisterRequest;
import com.hfut.studyroom.entity.User;
import com.hfut.studyroom.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        // 隐藏密码
        user.setPassword(null);
        return ApiResponse.success("注册成功", user);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ApiResponse.success("登录成功", loginResponse);
    }
}
