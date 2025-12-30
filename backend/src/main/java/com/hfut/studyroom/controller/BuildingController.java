package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.entity.Building;
import com.hfut.studyroom.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 教学楼控制器
 */
@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingController {
    
    private final BuildingRepository buildingRepository;
    
    /**
     * 获取所有教学楼列表
     */
    @GetMapping
    public ApiResponse<List<Building>> getAllBuildings() {
        List<Building> buildings = buildingRepository.findAll();
        return ApiResponse.success(buildings);
    }
    
    /**
     * 获取教学楼详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Building> getBuildingById(@PathVariable Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("教学楼不存在"));
        return ApiResponse.success(building);
    }
}
