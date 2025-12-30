package com.hfut.studyroom.dto;

import com.hfut.studyroom.entity.Classroom;
import lombok.Data;

/**
 * 教室传输对象
 */
@Data
public class ClassroomDTO {
    
    private Long id;
    private Long buildingId;
    private String buildingName;
    private String roomNumber;
    private Integer floor;
    private Integer capacity;
    private String status;
    
    /**
     * 从实体转换为DTO
     */
    public static ClassroomDTO fromEntity(Classroom classroom) {
        ClassroomDTO dto = new ClassroomDTO();
        dto.setId(classroom.getId());
        dto.setBuildingId(classroom.getBuilding().getId());
        dto.setBuildingName(classroom.getBuilding().getName());
        dto.setRoomNumber(classroom.getRoomNumber());
        dto.setFloor(classroom.getFloor());
        dto.setCapacity(classroom.getCapacity());
        dto.setStatus(classroom.getStatus());
        return dto;
    }
}
