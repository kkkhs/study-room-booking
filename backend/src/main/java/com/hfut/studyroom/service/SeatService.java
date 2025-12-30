package com.hfut.studyroom.service;

import com.hfut.studyroom.dto.SeatDTO;
import com.hfut.studyroom.entity.Seat;
import com.hfut.studyroom.repository.BookingRepository;
import com.hfut.studyroom.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位服务类
 */
@Service
@RequiredArgsConstructor
public class SeatService {
    
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final com.hfut.studyroom.repository.ClassroomOccupancyRepository occupancyRepository;
    private final com.hfut.studyroom.repository.ClassroomRepository classroomRepository;
    
    /**
     * 获取教室的座位列表
     */
    public List<Seat> getClassroomSeats(Long classroomId) {
        return seatRepository.findByClassroomId(classroomId);
    }
    
    /**
     * 获取可用座位列表
     */
    public List<Seat> getAvailableSeats(
            Long buildingId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return seatRepository.findAvailableSeats(buildingId, date, startTime, endTime);
    }
    
    /**
     * 获取指定教室的可用座位
     */
    public List<Seat> getAvailableSeatsByClassroom(
            Long classroomId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return seatRepository.findAvailableSeatsByClassroom(classroomId, date, startTime, endTime);
    }
    
    /**
     * 获取教室所有座位及预约状态
     */
    public List<SeatDTO> getClassroomSeatsWithStatus(
            Long classroomId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Seat> allSeats = seatRepository.findByClassroomId(classroomId);
        
        // 如果没有提供时间参数，所有座位都显示为可用
        if (date == null || startTime == null || endTime == null) {
            return allSeats.stream()
                    .map(SeatDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        
        // 检查教室是否被占用（如上课、会议等）
        boolean classroomOccupied = occupancyRepository.isClassroomOccupied(
                classroomId, date, startTime, endTime);
        
        // 如果教室被占用，所有座位都不可用
        if (classroomOccupied) {
            return allSeats.stream()
                    .map(seat -> {
                        SeatDTO dto = SeatDTO.fromEntity(seat);
                        dto.setStatus("OCCUPIED");
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        
        // 获取可用座位列表
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByClassroom(
                classroomId, date, startTime, endTime);
        
        // 转换为DTO并设置状态
        return allSeats.stream()
                .map(seat -> {
                    SeatDTO dto = SeatDTO.fromEntity(seat);
                    // 检查座位是否在可用列表中
                    boolean isAvailable = availableSeats.stream()
                            .anyMatch(s -> s.getId().equals(seat.getId()));
                    dto.setStatus(isAvailable ? "AVAILABLE" : "OCCUPIED");
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
