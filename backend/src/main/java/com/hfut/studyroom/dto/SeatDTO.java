package com.hfut.studyroom.dto;

import com.hfut.studyroom.entity.Seat;
import lombok.Data;

/**
 * 座位传输对象
 */
@Data
public class SeatDTO {
    
    private Long id;
    private Long classroomId;
    private String classroomName;
    private String buildingName;
    private String seatNumber;
    private Integer rowNum;
    private Integer colNum;
    private String location;
    private String status;
    
    /**
     * 从实体转换为DTO
     */
    public static SeatDTO fromEntity(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setClassroomId(seat.getClassroom().getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setRowNum(seat.getRowNum());
        dto.setColNum(seat.getColNum());
        
        // 构建位置描述
        String buildingName = seat.getClassroom().getBuilding().getName();
        String roomNumber = seat.getClassroom().getRoomNumber();
        dto.setBuildingName(buildingName);
        dto.setClassroomName(roomNumber + "教室");
        dto.setLocation("第" + seat.getRowNum() + "排第" + seat.getColNum() + "列");
        dto.setStatus("AVAILABLE");
        
        return dto;
    }
}
