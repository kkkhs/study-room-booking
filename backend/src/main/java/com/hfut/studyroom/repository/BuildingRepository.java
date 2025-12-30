package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 教学楼数据访问层
 */
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    
    /**
     * 根据编码查找教学楼
     */
    Building findByCode(String code);
}
