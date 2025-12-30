package com.hfut.studyroom.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "app_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名（学号）
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * 密码（BCrypt加密）
     */
    @Column(nullable = false, length = 100)
    private String password;
    
    /**
     * 真实姓名
     */
    @JsonProperty("name")
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;
    
    /**
     * 学号
     */
    @Column(name = "student_id", unique = true, length = 50)
    private String studentId;
    
    /**
     * 手机号
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 邮箱
     */
    @Column(length = 50)
    private String email;
    
    /**
     * 角色：USER-普通用户，ADMIN-管理员
     */
    @Column(nullable = false, length = 20)
    private String role = "USER";
    
    /**
     * 状态：ACTIVE-正常，DISABLED-禁用
     */
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
    
    /**
     * 违约次数
     */
    @Column(name = "violation_count", nullable = false)
    private Integer violationCount = 0;
    
    /**
     * 最后登录时间
     */
    @JsonProperty("lastLoginTime")
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建时间
     */
    @JsonProperty("createdTime")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
