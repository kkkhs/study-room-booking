package com.hfut.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hfut.studyroom.entity.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;

/**
 * 用户传输对象
 */
@Data
public class UserDTO {
    
    private Long id;
    private String username;
    
    @JsonProperty("name")
    private String realName;
    
    private String studentId;
    private String phone;
    private String email;
    private String role;
    private String status;
    private Integer violationCount;
    
    @JsonProperty("createdTime")
    private String createdAt;
    
    @JsonProperty("lastLoginTime")
    private String lastLoginTime;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 从实体转换为DTO
     */
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setStudentId(user.getStudentId());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setViolationCount(user.getViolationCount());
        dto.setCreatedAt(user.getCreatedAt().format(FORMATTER));
        if (user.getLastLoginTime() != null) {
            dto.setLastLoginTime(user.getLastLoginTime().format(FORMATTER));
        }
        return dto;
    }
}
