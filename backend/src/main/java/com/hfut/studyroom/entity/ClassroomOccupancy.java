package com.hfut.studyroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 教室占用记录实体
 */
@Entity
@Table(name = "classroom_occupancy")
@Data
public class ClassroomOccupancy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 教室
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;
    
    /**
     * 占用日期
     */
    @Column(nullable = false)
    private LocalDate occupancyDate;
    
    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalTime startTime;
    
    /**
     * 结束时间
     */
    @Column(nullable = false)
    private LocalTime endTime;
    
    /**
     * 占用原因（如：高等数学课、会议、活动等）
     */
    @Column(nullable = false)
    private String reason;
    
    /**
     * 占用类型（COURSE-课程, MEETING-会议, EVENT-活动, MAINTENANCE-维护）
     */
    @Column(nullable = false)
    private String type;
    
    /**
     * 占用人/负责人
     */
    private String occupiedBy;
    
    /**
     * 状态（SCHEDULED-已安排, ONGOING-进行中, COMPLETED-已完成, CANCELLED-已取消）
     */
    @Column(nullable = false)
    private String status;
    
    /**
     * 备注
     */
    @Column(length = 500)
    private String remarks;
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
