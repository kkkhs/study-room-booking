package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 预约数据访问层
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * 查找用户的所有预约
     */
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 查找用户指定状态的预约
     */
    List<Booking> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
    
    /**
     * 查找指定日期的所有预约
     */
    List<Booking> findByBookingDate(LocalDate date);
    
    /**
     * 查找指定状态的所有预约
     */
    List<Booking> findByStatus(String status);
    
    /**
     * 查找超时未签到的预约（PENDING状态且创建时间超过指定时间）
     */
    List<Booking> findByStatusAndCreatedAtBefore(String status, LocalDateTime time);
    
    /**
     * 查找已到期的活动预约（ACTIVE状态且结束时间已过）
     */
    @Query("SELECT b FROM Booking b " +
           "WHERE b.status = 'ACTIVE' " +
           "AND FUNCTION('CONCAT', b.bookingDate, ' ', b.endTime) < :now")
    List<Booking> findExpiredActiveBookings(@Param("now") LocalDateTime now);
    
    /**
     * 检查座位在指定时间段是否已被预约
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.seat.id = :seatId " +
           "AND b.bookingDate = :date " +
           "AND b.status IN ('PENDING', 'ACTIVE') " +
           "AND NOT (b.endTime <= :startTime OR b.startTime >= :endTime)")
    boolean existsConflictBooking(
        @Param("seatId") Long seatId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
    
    /**
     * 检查用户在指定时间段是否已有预约
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.user.id = :userId " +
           "AND b.bookingDate = :date " +
           "AND b.status IN ('PENDING', 'ACTIVE') " +
           "AND NOT (b.endTime <= :startTime OR b.startTime >= :endTime)")
    boolean existsUserBookingInTime(
        @Param("userId") Long userId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
    
    /**
     * 统计预约数量
     */
    long countByStatus(String status);
    
    /**
     * 统计今日预约数
     */
    long countByBookingDate(LocalDate date);
    
    /**
     * 统计用户指定状态的预约数量
     */
    long countByUserIdAndStatusIn(Long userId, List<String> statuses);
    
    /**
     * 统计用户在指定日期的指定状态预约数量
     */
    long countByUserIdAndBookingDateAndStatusIn(Long userId, LocalDate bookingDate, List<String> statuses);
}
