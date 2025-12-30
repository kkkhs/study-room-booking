package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.ClassroomOccupancyDTO;
import com.hfut.studyroom.dto.ClassroomOccupancyRequest;
import com.hfut.studyroom.entity.ClassroomOccupancy;
import com.hfut.studyroom.service.ClassroomOccupancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教室占用控制器
 */
@RestController
@RequestMapping("/occupancies")
@RequiredArgsConstructor
public class ClassroomOccupancyController {
    
    private final ClassroomOccupancyService occupancyService;
    
    /**
     * 创建教室占用记录（管理员）
     */
    @PostMapping
    public ApiResponse<ClassroomOccupancyDTO> createOccupancy(@Valid @RequestBody ClassroomOccupancyRequest request) {
        ClassroomOccupancy occupancy = occupancyService.createOccupancy(request);
        return ApiResponse.success("创建成功", ClassroomOccupancyDTO.fromEntity(occupancy));
    }
    
    /**
     * 更新教室占用记录（管理员）
     */
    @PutMapping("/{id}")
    public ApiResponse<ClassroomOccupancyDTO> updateOccupancy(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomOccupancyRequest request) {
        ClassroomOccupancy occupancy = occupancyService.updateOccupancy(id, request);
        return ApiResponse.success("更新成功", ClassroomOccupancyDTO.fromEntity(occupancy));
    }
    
    /**
     * 取消教室占用（管理员）
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelOccupancy(@PathVariable Long id) {
        occupancyService.cancelOccupancy(id);
        return ApiResponse.success("取消成功", null);
    }
    
    /**
     * 删除教室占用记录（管理员）
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOccupancy(@PathVariable Long id) {
        occupancyService.deleteOccupancy(id);
        return ApiResponse.success("删除成功", null);
    }
    
    /**
     * 获取指定教室在指定日期的占用记录
     */
    @GetMapping("/classroom/{classroomId}")
    public ApiResponse<List<ClassroomOccupancyDTO>> getClassroomOccupancies(
            @PathVariable Long classroomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ClassroomOccupancy> occupancies = occupancyService.getClassroomOccupancies(classroomId, date);
        List<ClassroomOccupancyDTO> dtos = occupancies.stream()
                .map(ClassroomOccupancyDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }
    
    /**
     * 获取指定教学楼在指定日期的占用记录
     */
    @GetMapping("/building/{buildingId}")
    public ApiResponse<List<ClassroomOccupancyDTO>> getBuildingOccupancies(
            @PathVariable Long buildingId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ClassroomOccupancy> occupancies = occupancyService.getBuildingOccupancies(buildingId, date);
        List<ClassroomOccupancyDTO> dtos = occupancies.stream()
                .map(ClassroomOccupancyDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }
    
    /**
     * 获取今日所有占用记录
     */
    @GetMapping("/today")
    public ApiResponse<List<ClassroomOccupancyDTO>> getTodayOccupancies() {
        List<ClassroomOccupancy> occupancies = occupancyService.getTodayOccupancies();
        List<ClassroomOccupancyDTO> dtos = occupancies.stream()
                .map(ClassroomOccupancyDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }
    
    /**
     * 获取所有占用记录（管理员）
     */
    @GetMapping
    public ApiResponse<List<ClassroomOccupancyDTO>> getAllOccupancies() {
        List<ClassroomOccupancy> occupancies = occupancyService.getAllOccupancies();
        List<ClassroomOccupancyDTO> dtos = occupancies.stream()
                .map(ClassroomOccupancyDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }
}
