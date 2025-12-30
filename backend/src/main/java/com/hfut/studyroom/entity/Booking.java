package com.hfut.studyroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 预约实体类
 */
@Data
@Entity
@Table(name = "booking")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 预约用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 预约座位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    /**
     * 预约日期
     */
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;
    
    /**
     * 开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    /**
     * 结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    /**
     * 预约状态
     * PENDING-待签到
     * ACTIVE-使用中
     * COMPLETED-已完成
     * CANCELLED-已取消
     * TIMEOUT-超时未签到
     */
    @Column(nullable = false, length = 20)
    private String status = "PENDING";
    
    /**
     * 签到时间
     */
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
    
    /**
     * 签退时间
     */
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;
    
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
