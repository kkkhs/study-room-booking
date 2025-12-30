package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.ClassroomOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 教室占用记录数据访问层
 */
@Repository
public interface ClassroomOccupancyRepository extends JpaRepository<ClassroomOccupancy, Long> {
    
    /**
     * 查找指定教室在指定日期的所有占用记录
     */
    List<ClassroomOccupancy> findByClassroomIdAndOccupancyDateOrderByStartTime(Long classroomId, LocalDate date);
    
    /**
     * 查找指定教学楼在指定日期的所有占用记录
     */
    @Query("SELECT co FROM ClassroomOccupancy co " +
           "WHERE co.classroom.building.id = :buildingId " +
           "AND co.occupancyDate = :date " +
           "AND co.status IN ('SCHEDULED', 'ONGOING') " +
           "ORDER BY co.classroom.roomNumber, co.startTime")
    List<ClassroomOccupancy> findByBuildingAndDate(
        @Param("buildingId") Long buildingId,
        @Param("date") LocalDate date
    );
    
    /**
     * 检查教室在指定时间段是否被占用
     */
    @Query("SELECT COUNT(co) > 0 FROM ClassroomOccupancy co " +
           "WHERE co.classroom.id = :classroomId " +
           "AND co.occupancyDate = :date " +
           "AND co.status IN ('SCHEDULED', 'ONGOING') " +
           "AND NOT (co.endTime <= :startTime OR co.startTime >= :endTime)")
    boolean isClassroomOccupied(
        @Param("classroomId") Long classroomId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
    
    /**
     * 查找指定日期范围内的所有占用记录
     */
    @Query("SELECT co FROM ClassroomOccupancy co " +
           "WHERE co.occupancyDate BETWEEN :startDate AND :endDate " +
           "AND co.status IN ('SCHEDULED', 'ONGOING') " +
           "ORDER BY co.occupancyDate, co.startTime")
    List<ClassroomOccupancy> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * 查找今日所有占用记录
     */
    @Query("SELECT co FROM ClassroomOccupancy co " +
           "WHERE co.occupancyDate = :today " +
           "AND co.status IN ('SCHEDULED', 'ONGOING') " +
           "ORDER BY co.classroom.building.name, co.classroom.roomNumber, co.startTime")
    List<ClassroomOccupancy> findTodayOccupancies(@Param("today") LocalDate today);
}
