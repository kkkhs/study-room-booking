package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.SeatDTO;
import com.hfut.studyroom.entity.Seat;
import com.hfut.studyroom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位控制器
 */
@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {
    
    private final SeatService seatService;
    
    /**
     * 获取可用座位列表
     * @param buildingId 教学楼ID
     * @param classroomId 教室ID（可选，如果指定则只查该教室）
     * @param date 日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @GetMapping("/available")
    public ApiResponse<List<SeatDTO>> getAvailableSeats(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long classroomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime
    ) {
        List<Seat> seats;
        
        if (classroomId != null) {
            // 查询指定教室的可用座位
            seats = seatService.getAvailableSeatsByClassroom(classroomId, date, startTime, endTime);
        } else if (buildingId != null) {
            // 查询指定教学楼的可用座位
            seats = seatService.getAvailableSeats(buildingId, date, startTime, endTime);
        } else {
            throw new RuntimeException("请指定教学楼或教室");
        }
        
        // 转换为DTO
        List<SeatDTO> seatDTOs = seats.stream()
                .map(SeatDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success(seatDTOs);
    }
}
