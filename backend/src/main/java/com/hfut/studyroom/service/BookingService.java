package com.hfut.studyroom.service;

import com.hfut.studyroom.dto.BookingRequest;
import com.hfut.studyroom.entity.Booking;
import com.hfut.studyroom.entity.Seat;
import com.hfut.studyroom.entity.User;
import com.hfut.studyroom.exception.BusinessException;
import com.hfut.studyroom.repository.BlacklistRepository;
import com.hfut.studyroom.repository.BookingRepository;
import com.hfut.studyroom.repository.SeatRepository;
import com.hfut.studyroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约服务类
 */
@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final BlacklistRepository blacklistRepository;
    
    /**
     * 创建预约
     */
    @Transactional
    public Booking createBooking(Long userId, BookingRequest request) {
        // 1. 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 2. 检查用户是否在黑名单
        if (blacklistRepository.existsByUserId(userId)) {
            throw new BusinessException("您已被加入黑名单，无法预约");
        }
        
        // 3. 检查用户当天是否已有有效预约（PENDING或ACTIVE状态）
        long sameDayBookingCount = bookingRepository.countByUserIdAndBookingDateAndStatusIn(
                userId,
                request.getBookingDate(),
                java.util.Arrays.asList("PENDING", "ACTIVE"));
        if (sameDayBookingCount > 0) {
            throw new BusinessException("您在当天已有预约，每天只能预约一次");
        }
        
        // 4. 检查座位是否存在
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new BusinessException("座位不存在"));
        
        // 5. 检查座位是否已被预约（时间冲突）
        if (bookingRepository.existsConflictBooking(
                request.getSeatId(),
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime())) {
            throw new BusinessException("该座位在此时间段已被预约");
        }
        
        // 6. 检查用户在该时间段是否已有其他预约
        if (bookingRepository.existsUserBookingInTime(
                userId,
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime())) {
            throw new BusinessException("您在此时间段已有其他预约");
        }
        
        // 7. 创建预约
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setBookingDate(request.getBookingDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus("PENDING");
        
        return bookingRepository.save(booking);
    }
    
    /**
     * 获取用户的预约列表
     */
    public List<Booking> getMyBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * 签到
     */
    @Transactional
    public Booking checkIn(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        
        // 检查是否是本人的预约
        if (!booking.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作此预约");
        }
        
        // 检查预约状态
        if (!"PENDING".equals(booking.getStatus())) {
            String statusMsg = switch (booking.getStatus()) {
                case "ACTIVE" -> "该预约已签到，无需重复签到";
                case "COMPLETED" -> "该预约已完成，无法签到";
                case "CANCELLED" -> "该预约已取消，无法签到";
                case "VIOLATED" -> "该预约已违约，无法签到";
                default -> "该预约状态异常，无法签到";
            };
            throw new BusinessException(statusMsg);
        }
        
        // 检查签到时间（预约开始时间前30分钟到预约时间之间可以签到）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getStartTime());
        LocalDateTime earliestCheckIn = bookingDateTime.minusMinutes(30);
        
        if (now.isBefore(earliestCheckIn)) {
            throw new BusinessException("签到时间未到，请在预约开始前30分钟内签到");
        }
        
        if (now.isAfter(bookingDateTime.plusMinutes(15))) {
            // 超过预约开始时间15分钟，标记为违约
            booking.setStatus("VIOLATED");
            bookingRepository.save(booking);
            throw new BusinessException("签到已超时，预约已自动取消");
        }
        
        // 更新状态
        booking.setStatus("ACTIVE");
        booking.setCheckInTime(LocalDateTime.now());
        
        return bookingRepository.save(booking);
    }
    
    /**
     * 签退
     */
    @Transactional
    public Booking checkOut(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        
        // 检查是否是本人的预约
        if (!booking.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作此预约");
        }
        
        // 检查预约状态
        if (!"ACTIVE".equals(booking.getStatus())) {
            throw new BusinessException("该预约无法签退");
        }
        
        // 更新状态
        booking.setStatus("COMPLETED");
        booking.setCheckOutTime(LocalDateTime.now());
        
        return bookingRepository.save(booking);
    }
    
    /**
     * 取消预约
     */
    @Transactional
    public Booking cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        
        // 检查是否是本人的预约
        if (!booking.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作此预约");
        }
        
        // 只有PENDING状态才能取消
        if (!"PENDING".equals(booking.getStatus())) {
            throw new BusinessException("该预约无法取消");
        }
        
        // 更新状态
        booking.setStatus("CANCELLED");
        
        return bookingRepository.save(booking);
    }
}
