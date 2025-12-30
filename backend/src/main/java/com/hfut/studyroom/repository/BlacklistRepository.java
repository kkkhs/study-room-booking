package com.hfut.studyroom.repository;

import com.hfut.studyroom.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 黑名单数据访问层
 */
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    
    /**
     * 检查用户是否在黑名单中
     */
    boolean existsByUserId(Long userId);
    
    /**
     * 根据用户ID查找黑名单记录
     */
    Blacklist findByUserId(Long userId);
    
    /**
     * 删除用户的黑名单记录
     */
    void deleteByUserId(Long userId);
}
