package com.hfut.studyroom.scheduler;

import com.hfut.studyroom.entity.Booking;
import com.hfut.studyroom.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingScheduler {
    
    private final BookingRepository bookingRepository;
    
    @Value("${app.booking.timeout-minutes:15}")
    private int timeoutMinutes;
    
    /**
     * 超时释放座位
     * 每分钟执行一次，将开始时间后15分钟仍未签到的PENDING预约改为TIMEOUT
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void releaseTimeoutBookings() {
        LocalDateTime now = LocalDateTime.now();
        
        // 查找所有 PENDING 状态的预约
        List<Booking> pendingBookings = bookingRepository.findByStatus("PENDING");
        
        List<Booking> timeoutBookings = pendingBookings.stream()
                .filter(booking -> {
                    // 计算预约开始时间
                    LocalDateTime startDateTime = LocalDateTime.of(
                            booking.getBookingDate(), 
                            booking.getStartTime()
                    );
                    // 如果当前时间超过开始时间 + 15分钟，则超时
                    LocalDateTime timeoutThreshold = startDateTime.plusMinutes(timeoutMinutes);
                    return now.isAfter(timeoutThreshold);
                })
                .toList();
        
        if (!timeoutBookings.isEmpty()) {
            timeoutBookings.forEach(booking -> booking.setStatus("TIMEOUT"));
            bookingRepository.saveAll(timeoutBookings);
            log.info("释放{}个超时未签到的预约", timeoutBookings.size());
        }
    }
    
    /**
     * 自动完成已到期的预约
     * 每小时执行一次，将已过结束时间的ACTIVE预约改为COMPLETED
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void completeExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        
        List<Booking> expiredBookings = bookingRepository.findExpiredActiveBookings(now);
        
        if (!expiredBookings.isEmpty()) {
            expiredBookings.forEach(booking -> {
                booking.setStatus("COMPLETED");
                booking.setCheckOutTime(LocalDateTime.of(
                        booking.getBookingDate(),
                        booking.getEndTime()
                ));
            });
            bookingRepository.saveAll(expiredBookings);
            log.info("自动完成{}个已到期的预约", expiredBookings.size());
        }
    }
}
