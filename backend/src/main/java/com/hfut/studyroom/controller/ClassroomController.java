package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.ClassroomDTO;
import com.hfut.studyroom.dto.SeatDTO;
import com.hfut.studyroom.entity.Classroom;
import com.hfut.studyroom.repository.ClassroomRepository;
import com.hfut.studyroom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教室控制器
 */
@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {
    
    private final ClassroomRepository classroomRepository;
    private final SeatService seatService;
    
    /**
     * 获取教室列表
     * @param buildingId 教学楼ID（可选）
     */
    @GetMapping
    public ApiResponse<List<ClassroomDTO>> getClassrooms(
            @RequestParam(required = false) Long buildingId
    ) {
        List<Classroom> classrooms;
        if (buildingId != null) {
            classrooms = classroomRepository.findByBuildingId(buildingId);
        } else {
            classrooms = classroomRepository.findAll();
        }
        
        List<ClassroomDTO> classroomDTOs = classrooms.stream()
                .map(ClassroomDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success(classroomDTOs);
    }
    
    /**
     * 获取教室详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ClassroomDTO> getClassroomById(@PathVariable Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("教室不存在"));
        return ApiResponse.success(ClassroomDTO.fromEntity(classroom));
    }
    
    /**
     * 获取教室座位列表（带预约状态）
     */
    @GetMapping("/{id}/seats")
    public ApiResponse<List<SeatDTO>> getClassroomSeats(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime
    ) {
        List<SeatDTO> seats = seatService.getClassroomSeatsWithStatus(id, date, startTime, endTime);
        return ApiResponse.success(seats);
    }
}
