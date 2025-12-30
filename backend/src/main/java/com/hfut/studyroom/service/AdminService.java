package com.hfut.studyroom.service;

import com.hfut.studyroom.entity.*;
import com.hfut.studyroom.exception.BusinessException;
import com.hfut.studyroom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理服务类
 */
@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final ClassroomRepository classroomRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BlacklistRepository blacklistRepository;
    
    /**
     * 创建教室
     */
    @Transactional
    public Classroom createClassroom(Classroom classroom) {
        // 检查教室号是否已存在
        if (classroomRepository.findByBuildingIdAndRoomNumber(
                classroom.getBuilding().getId(),
                classroom.getRoomNumber()) != null) {
            throw new BusinessException("该教室已存在");
        }
        
        return classroomRepository.save(classroom);
    }
    
    /**
     * 更新教室
     */
    @Transactional
    public Classroom updateClassroom(Long id, Classroom classroom) {
        Classroom existing = classroomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("教室不存在"));
        
        existing.setRoomNumber(classroom.getRoomNumber());
        existing.setFloor(classroom.getFloor());
        existing.setCapacity(classroom.getCapacity());
        existing.setStatus(classroom.getStatus());
        
        return classroomRepository.save(existing);
    }
    
    /**
     * 删除教室
     */
    @Transactional
    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new BusinessException("教室不存在");
        }
        classroomRepository.deleteById(id);
    }
    
    /**
     * 初始化教室座位（10行10列，共100个座位）
     */
    @Transactional
    public void initClassroomSeats(Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new BusinessException("教室不存在"));
        
        // 检查是否已有座位
        if (seatRepository.countByClassroomId(classroomId) > 0) {
            throw new BusinessException("该教室已初始化座位");
        }
        
        // 生成10行10列座位
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                Seat seat = new Seat();
                seat.setClassroom(classroom);
                seat.setSeatNumber(String.format("%c%02d", 'A' + row - 1, col));
                seat.setRowNum(row);
                seat.setColNum(col);
                seatRepository.save(seat);
            }
        }
    }
    
    /**
     * 获取所有预约记录
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * 删除预约记录
     */
    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BusinessException("预约不存在");
        }
        bookingRepository.deleteById(id);
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * 更新用户状态
     */
    @Transactional
    public User updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        user.setStatus(status);
        return userRepository.save(user);
    }
    
    /**
     * 添加黑名单
     */
    @Transactional
    public Blacklist addBlacklist(Long userId, String reason, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new BusinessException("管理员不存在"));
        
        // 检查是否已在黑名单
        if (blacklistRepository.existsByUserId(userId)) {
            throw new BusinessException("该用户已在黑名单中");
        }
        
        Blacklist blacklist = new Blacklist();
        blacklist.setUser(user);
        blacklist.setReason(reason);
        blacklist.setCreatedBy(admin);
        
        return blacklistRepository.save(blacklist);
    }
    
    /**
     * 移除黑名单
     */
    @Transactional
    public void removeBlacklist(Long id) {
        if (!blacklistRepository.existsById(id)) {
            throw new BusinessException("黑名单记录不存在");
        }
        blacklistRepository.deleteById(id);
    }
    
    /**
     * 获取黑名单列表
     */
    public List<Blacklist> getAllBlacklist() {
        return blacklistRepository.findAll();
    }
    
    /**
     * 获取统计数据
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("todayBookings", bookingRepository.countByBookingDate(LocalDate.now()));
        stats.put("activeBookings", bookingRepository.countByStatus("ACTIVE"));
        return stats;
    }
}
