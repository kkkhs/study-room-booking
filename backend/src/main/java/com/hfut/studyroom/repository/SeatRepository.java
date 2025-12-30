package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 座位数据访问层
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    /**
     * 根据教室ID查找所有座位
     */
    List<Seat> findByClassroomId(Long classroomId);
    
    /**
     * 统计教室座位数
     */
    long countByClassroomId(Long classroomId);
    
    /**
     * 查询可用座位（未在指定时间段被预约的座位）
     */
    @Query("SELECT s FROM Seat s " +
           "WHERE s.classroom.building.id = :buildingId " +
           "AND s.classroom.status = 'OPEN' " +
           "AND NOT EXISTS (" +
           "  SELECT 1 FROM Booking b " +
           "  WHERE b.seat.id = s.id " +
           "  AND b.bookingDate = :date " +
           "  AND b.status IN ('PENDING', 'ACTIVE') " +
           "  AND NOT (b.endTime <= :startTime OR b.startTime >= :endTime)" +
           ")")
    List<Seat> findAvailableSeats(
        @Param("buildingId") Long buildingId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
    
    /**
     * 查询指定教室的可用座位
     */
    @Query("SELECT s FROM Seat s " +
           "WHERE s.classroom.id = :classroomId " +
           "AND NOT EXISTS (" +
           "  SELECT 1 FROM Booking b " +
           "  WHERE b.seat.id = s.id " +
           "  AND b.bookingDate = :date " +
           "  AND b.status IN ('PENDING', 'ACTIVE') " +
           "  AND NOT (b.endTime <= :startTime OR b.startTime >= :endTime)" +
           ")")
    List<Seat> findAvailableSeatsByClassroom(
        @Param("classroomId") Long classroomId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
