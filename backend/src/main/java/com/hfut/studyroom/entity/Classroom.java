package com.hfut.studyroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教室实体类
 */
@Data
@Entity
@Table(name = "classroom", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"building_id", "room_number"}))
public class Classroom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 所属教学楼
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;
    
    /**
     * 教室号（如：102）
     */
    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;
    
    /**
     * 楼层
     */
    @Column(nullable = false)
    private Integer floor;
    
    /**
     * 座位总数
     */
    @Column(nullable = false)
    private Integer capacity = 100;
    
    /**
     * 状态：OPEN-开放，CLOSED-关闭
     */
    @Column(nullable = false, length = 20)
    private String status = "OPEN";
    
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
