package com.hfut.studyroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教学楼实体类
 */
@Data
@Entity
@Table(name = "building")
public class Building {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 教学楼名称（如：新安学堂）
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    /**
     * 教学楼编码（如：XA）
     */
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    
    /**
     * 楼层数
     */
    @Column(nullable = false)
    private Integer floors;
    
    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
