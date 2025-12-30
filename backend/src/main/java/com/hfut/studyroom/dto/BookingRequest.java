package com.hfut.studyroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 预约请求
 */
@Data
public class BookingRequest {
    
    @NotNull(message = "座位ID不能为空")
    private Long seatId;
    
    @NotBlank(message = "开始时间不能为空")
    private String startTime;
    
    @NotBlank(message = "结束时间不能为空")
    private String endTime;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 获取预约日期
     */
    public LocalDate getBookingDate() {
        return LocalDateTime.parse(startTime, FORMATTER).toLocalDate();
    }
    
    /**
     * 获取开始时间
     */
    public LocalTime getStartTime() {
        return LocalDateTime.parse(startTime, FORMATTER).toLocalTime();
    }
    
    /**
     * 获取结束时间
     */
    public LocalTime getEndTime() {
        return LocalDateTime.parse(endTime, FORMATTER).toLocalTime();
    }
}
