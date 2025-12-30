package com.hfut.studyroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 教室占用创建/更新请求
 */
@Data
public class ClassroomOccupancyRequest {
    
    @NotNull(message = "教室ID不能为空")
    private Long classroomId;
    
    @NotBlank(message = "占用日期不能为空")
    private String occupancyDate;
    
    @NotBlank(message = "开始时间不能为空")
    private String startTime;
    
    @NotBlank(message = "结束时间不能为空")
    private String endTime;
    
    @NotBlank(message = "占用原因不能为空")
    private String reason;
    
    @NotBlank(message = "占用类型不能为空")
    private String type; // COURSE, MEETING, EVENT, MAINTENANCE
    
    private String occupiedBy;
    
    private String remarks;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * 获取日期对象
     */
    public LocalDate getOccupancyDateAsLocalDate() {
        return LocalDate.parse(occupancyDate, DATE_FORMATTER);
    }
    
    /**
     * 获取开始时间对象
     */
    public LocalTime getStartTimeAsLocalTime() {
        return LocalTime.parse(startTime, TIME_FORMATTER);
    }
    
    /**
     * 获取结束时间对象
     */
    public LocalTime getEndTimeAsLocalTime() {
        return LocalTime.parse(endTime, TIME_FORMATTER);
    }
}
