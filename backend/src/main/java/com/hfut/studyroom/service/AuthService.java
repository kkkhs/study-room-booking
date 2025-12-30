package com.hfut.studyroom.service;

import com.hfut.studyroom.dto.LoginRequest;
import com.hfut.studyroom.dto.LoginResponse;
import com.hfut.studyroom.dto.RegisterRequest;
import com.hfut.studyroom.entity.User;
import com.hfut.studyroom.exception.BusinessException;
import com.hfut.studyroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 认证服务类
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 用户注册
     */
    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已被注册");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getName());
        user.setStudentId(request.getStudentId());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setStatus("ACTIVE");
        
        return userRepository.save(user);
    }
    
    /**
     * 用户登录
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查用户状态
        if ("DISABLED".equals(user.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(java.time.LocalDateTime.now());
        userRepository.save(user);
        
        // 生成token (简单实现，实际项目应使用JWT)
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 创建返回对象（不包含密码）
        User userResponse = new User();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setRealName(user.getRealName());
        userResponse.setStudentId(user.getStudentId());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        userResponse.setViolationCount(user.getViolationCount());
        userResponse.setLastLoginTime(user.getLastLoginTime());
        userResponse.setCreatedAt(user.getCreatedAt());
        
        return new LoginResponse(token, userResponse);
    }
}
