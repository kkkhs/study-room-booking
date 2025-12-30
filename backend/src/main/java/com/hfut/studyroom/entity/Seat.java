package com.hfut.studyroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 座位实体类
 */
@Data
@Entity
@Table(name = "seat",
       uniqueConstraints = @UniqueConstraint(columnNames = {"classroom_id", "seat_number"}))
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 所属教室
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;
    
    /**
     * 座位号（如：A01）
     */
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;
    
    /**
     * 行号（1-10）
     */
    @Column(name = "row_num", nullable = false)
    private Integer rowNum;
    
    /**
     * 列号（1-10）
     */
    @Column(name = "col_num", nullable = false)
    private Integer colNum;
    
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
