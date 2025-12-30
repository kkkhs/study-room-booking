package com.hfut.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hfut.studyroom.entity.ClassroomOccupancy;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 教室占用记录传输对象
 */
@Data
public class ClassroomOccupancyDTO {
    
    private Long id;
    private Long classroomId;
    private String classroomName;
    private String buildingName;
    private String occupancyDate;
    private String startTime;
    private String endTime;
    private String reason;
    private String type;
    private String occupiedBy;
    private String status;
    private String remarks;
    
    @JsonProperty("createdTime")
    private String createdAt;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 从实体转换为DTO
     */
    public static ClassroomOccupancyDTO fromEntity(ClassroomOccupancy occupancy) {
        ClassroomOccupancyDTO dto = new ClassroomOccupancyDTO();
        dto.setId(occupancy.getId());
        dto.setClassroomId(occupancy.getClassroom().getId());
        dto.setClassroomName(occupancy.getClassroom().getRoomNumber() + "教室");
        dto.setBuildingName(occupancy.getClassroom().getBuilding().getName());
        dto.setOccupancyDate(occupancy.getOccupancyDate().format(DATE_FORMATTER));
        dto.setStartTime(occupancy.getStartTime().format(TIME_FORMATTER));
        dto.setEndTime(occupancy.getEndTime().format(TIME_FORMATTER));
        dto.setReason(occupancy.getReason());
        dto.setType(mapType(occupancy.getType()));
        dto.setOccupiedBy(occupancy.getOccupiedBy());
        dto.setStatus(mapStatus(occupancy.getStatus()));
        dto.setRemarks(occupancy.getRemarks());
        dto.setCreatedAt(occupancy.getCreatedAt().format(DATETIME_FORMATTER));
        return dto;
    }
    
    /**
     * 类型映射
     */
    private static String mapType(String type) {
        return switch (type) {
            case "COURSE" -> "课程";
            case "MEETING" -> "会议";
            case "EVENT" -> "活动";
            case "MAINTENANCE" -> "维护";
            default -> type;
        };
    }
    
    /**
     * 状态映射
     */
    private static String mapStatus(String status) {
        return switch (status) {
            case "SCHEDULED" -> "已安排";
            case "ONGOING" -> "进行中";
            case "COMPLETED" -> "已完成";
            case "CANCELLED" -> "已取消";
            default -> status;
        };
    }
}
