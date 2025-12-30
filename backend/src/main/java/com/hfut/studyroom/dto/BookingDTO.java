package com.hfut.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hfut.studyroom.entity.Booking;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 预约传输对象
 */
@Data
public class BookingDTO {
    
    private Long id;
    private Long userId;
    private String username;
    private Long seatId;
    private String seatNumber;
    private String classroomName;
    private String buildingName;
    private String startTime;
    private String endTime;
    private String status;
    
    @JsonProperty("createdTime")
    private String createdAt;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 从实体转换为DTO
     */
    public static BookingDTO fromEntity(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setUsername(booking.getUser().getUsername());
        dto.setSeatId(booking.getSeat().getId());
        dto.setSeatNumber(booking.getSeat().getSeatNumber());
        dto.setClassroomName(booking.getSeat().getClassroom().getRoomNumber() + "教室");
        dto.setBuildingName(booking.getSeat().getClassroom().getBuilding().getName());
        
        // 组合日期和时间
        LocalDateTime startDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getEndTime());
        dto.setStartTime(startDateTime.format(FORMATTER));
        dto.setEndTime(endDateTime.format(FORMATTER));
        
        dto.setStatus(mapStatus(booking.getStatus()));
        dto.setCreatedAt(booking.getCreatedAt().format(FORMATTER));
        
        return dto;
    }
    
    /**
     * 状态映射（保持与后端一致）
     */
    private static String mapStatus(String status) {
        // 直接返回后端状态，前端统一处理
        return status;
    }
}
