package com.hfut.studyroom.service;

import com.hfut.studyroom.dto.ClassroomOccupancyRequest;
import com.hfut.studyroom.entity.Classroom;
import com.hfut.studyroom.entity.ClassroomOccupancy;
import com.hfut.studyroom.exception.BusinessException;
import com.hfut.studyroom.repository.ClassroomOccupancyRepository;
import com.hfut.studyroom.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 教室占用服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomOccupancyService {
    
    private final ClassroomOccupancyRepository occupancyRepository;
    private final ClassroomRepository classroomRepository;
    
    /**
     * 创建教室占用记录
     */
    @Transactional
    public ClassroomOccupancy createOccupancy(ClassroomOccupancyRequest request) {
        // 检查教室是否存在
        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new BusinessException("教室不存在"));
        
        // 检查时间冲突
        if (occupancyRepository.isClassroomOccupied(
                request.getClassroomId(),
                request.getOccupancyDateAsLocalDate(),
                request.getStartTimeAsLocalTime(),
                request.getEndTimeAsLocalTime())) {
            throw new BusinessException("该教室在此时间段已被占用");
        }
        
        // 创建占用记录
        ClassroomOccupancy occupancy = new ClassroomOccupancy();
        occupancy.setClassroom(classroom);
        occupancy.setOccupancyDate(request.getOccupancyDateAsLocalDate());
        occupancy.setStartTime(request.getStartTimeAsLocalTime());
        occupancy.setEndTime(request.getEndTimeAsLocalTime());
        occupancy.setReason(request.getReason());
        occupancy.setType(request.getType());
        occupancy.setOccupiedBy(request.getOccupiedBy());
        occupancy.setStatus("SCHEDULED");
        occupancy.setRemarks(request.getRemarks());
        
        return occupancyRepository.save(occupancy);
    }
    
    /**
     * 更新教室占用记录
     */
    @Transactional
    public ClassroomOccupancy updateOccupancy(Long id, ClassroomOccupancyRequest request) {
        ClassroomOccupancy occupancy = occupancyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("占用记录不存在"));
        
        // 如果修改了教室或时间，需要检查冲突
        if (!occupancy.getClassroom().getId().equals(request.getClassroomId()) ||
            !occupancy.getOccupancyDate().equals(request.getOccupancyDateAsLocalDate()) ||
            !occupancy.getStartTime().equals(request.getStartTimeAsLocalTime()) ||
            !occupancy.getEndTime().equals(request.getEndTimeAsLocalTime())) {
            
            // 检查教室是否存在
            Classroom classroom = classroomRepository.findById(request.getClassroomId())
                    .orElseThrow(() -> new BusinessException("教室不存在"));
            
            occupancy.setClassroom(classroom);
            occupancy.setOccupancyDate(request.getOccupancyDateAsLocalDate());
            occupancy.setStartTime(request.getStartTimeAsLocalTime());
            occupancy.setEndTime(request.getEndTimeAsLocalTime());
        }
        
        occupancy.setReason(request.getReason());
        occupancy.setType(request.getType());
        occupancy.setOccupiedBy(request.getOccupiedBy());
        occupancy.setRemarks(request.getRemarks());
        
        return occupancyRepository.save(occupancy);
    }
    
    /**
     * 取消教室占用
     */
    @Transactional
    public void cancelOccupancy(Long id) {
        ClassroomOccupancy occupancy = occupancyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("占用记录不存在"));
        
        occupancy.setStatus("CANCELLED");
        occupancyRepository.save(occupancy);
    }
    
    /**
     * 删除教室占用记录
     */
    @Transactional
    public void deleteOccupancy(Long id) {
        if (!occupancyRepository.existsById(id)) {
            throw new BusinessException("占用记录不存在");
        }
        occupancyRepository.deleteById(id);
    }
    
    /**
     * 获取指定教室在指定日期的占用记录
     */
    public List<ClassroomOccupancy> getClassroomOccupancies(Long classroomId, LocalDate date) {
        return occupancyRepository.findByClassroomIdAndOccupancyDateOrderByStartTime(classroomId, date);
    }
    
    /**
     * 获取指定教学楼在指定日期的占用记录
     */
    public List<ClassroomOccupancy> getBuildingOccupancies(Long buildingId, LocalDate date) {
        return occupancyRepository.findByBuildingAndDate(buildingId, date);
    }
    
    /**
     * 获取今日所有占用记录
     */
    public List<ClassroomOccupancy> getTodayOccupancies() {
        return occupancyRepository.findTodayOccupancies(LocalDate.now());
    }
    
    /**
     * 检查教室是否被占用
     */
    public boolean isClassroomOccupied(Long classroomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return occupancyRepository.isClassroomOccupied(classroomId, date, startTime, endTime);
    }
    
    /**
     * 获取所有占用记录
     */
    public List<ClassroomOccupancy> getAllOccupancies() {
        return occupancyRepository.findAll();
    }
}
