package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.BlacklistDTO;
import com.hfut.studyroom.dto.BookingDTO;
import com.hfut.studyroom.dto.UserDTO;
import com.hfut.studyroom.entity.*;
import com.hfut.studyroom.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    
    // ==================== 教室管理 ====================
    
    /**
     * 创建教室
     */
    @PostMapping("/classrooms")
    public ApiResponse<Classroom> createClassroom(@RequestBody Classroom classroom) {
        Classroom created = adminService.createClassroom(classroom);
        return ApiResponse.success("创建成功", created);
    }
    
    /**
     * 更新教室
     */
    @PutMapping("/classrooms/{id}")
    public ApiResponse<Classroom> updateClassroom(
            @PathVariable Long id,
            @RequestBody Classroom classroom
    ) {
        Classroom updated = adminService.updateClassroom(id, classroom);
        return ApiResponse.success("更新成功", updated);
    }
    
    /**
     * 删除教室
     */
    @DeleteMapping("/classrooms/{id}")
    public ApiResponse<Void> deleteClassroom(@PathVariable Long id) {
        adminService.deleteClassroom(id);
        return ApiResponse.success("删除成功", null);
    }
    
    /**
     * 初始化教室座位
     */
    @PostMapping("/classrooms/{id}/seats/init")
    public ApiResponse<Void> initClassroomSeats(@PathVariable Long id) {
        adminService.initClassroomSeats(id);
        return ApiResponse.success("初始化成功，已生成100个座位", null);
    }
    
    // ==================== 预约管理 ====================
    
    /**
     * 获取所有预约记录
     */
    @GetMapping("/bookings")
    public ApiResponse<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = adminService.getAllBookings();
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(bookingDTOs);
    }
    
    /**
     * 删除预约记录
     */
    @DeleteMapping("/bookings/{id}")
    public ApiResponse<Void> deleteBooking(@PathVariable Long id) {
        adminService.deleteBooking(id);
        return ApiResponse.success("删除成功", null);
    }
    
    // ==================== 用户管理 ====================
    
    /**
     * 获取所有用户
     */
    @GetMapping("/users")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(userDTOs);
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/users/{id}")
    public ApiResponse<User> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        User user = adminService.updateUserStatus(id, status);
        user.setPassword(null);
        return ApiResponse.success("更新成功", user);
    }
    
    // ==================== 黑名单管理 ====================
    
    /**
     * 获取黑名单列表
     */
    @GetMapping("/blacklist")
    public ApiResponse<List<BlacklistDTO>> getAllBlacklist() {
        List<Blacklist> blacklist = adminService.getAllBlacklist();
        List<BlacklistDTO> blacklistDTOs = blacklist.stream()
                .map(BlacklistDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(blacklistDTOs);
    }
    
    /**
     * 添加黑名单
     */
    @PostMapping("/blacklist")
    public ApiResponse<Blacklist> addBlacklist(
            @RequestParam Long userId,
            @RequestParam String reason,
            @RequestParam Long adminId
    ) {
        Blacklist blacklist = adminService.addBlacklist(userId, reason, adminId);
        return ApiResponse.success("添加成功", blacklist);
    }
    
    /**
     * 移除黑名单
     */
    @DeleteMapping("/blacklist/{id}")
    public ApiResponse<Void> removeBlacklist(@PathVariable Long id) {
        adminService.removeBlacklist(id);
        return ApiResponse.success("移除成功", null);
    }
    
    // ==================== 统计数据 ====================
    
    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = adminService.getStatistics();
        return ApiResponse.success(stats);
    }
}
