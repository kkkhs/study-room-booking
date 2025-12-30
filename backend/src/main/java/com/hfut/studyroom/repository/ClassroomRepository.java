package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 教室数据访问层
 */
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
    /**
     * 根据教学楼ID查找教室列表
     */
    List<Classroom> findByBuildingId(Long buildingId);
    
    /**
     * 根据教学楼ID和教室号查找
     */
    Classroom findByBuildingIdAndRoomNumber(Long buildingId, String roomNumber);
    
    /**
     * 统计教室数量
     */
    long countByBuildingId(Long buildingId);
}
