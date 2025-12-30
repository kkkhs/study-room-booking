package com.hfut.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hfut.studyroom.entity.Blacklist;
import lombok.Data;

import java.time.format.DateTimeFormatter;

/**
 * 黑名单传输对象
 */
@Data
public class BlacklistDTO {
    
    private Long id;
    private Long userId;
    private String username;
    private String realName;
    private String reason;
    private String createdByUsername;
    
    @JsonProperty("createdTime")
    private String createdAt;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 从实体转换为DTO
     */
    public static BlacklistDTO fromEntity(Blacklist blacklist) {
        BlacklistDTO dto = new BlacklistDTO();
        dto.setId(blacklist.getId());
        dto.setUserId(blacklist.getUser().getId());
        dto.setUsername(blacklist.getUser().getUsername());
        dto.setRealName(blacklist.getUser().getRealName());
        dto.setReason(blacklist.getReason());
        if (blacklist.getCreatedBy() != null) {
            dto.setCreatedByUsername(blacklist.getCreatedBy().getUsername());
        }
        dto.setCreatedAt(blacklist.getCreatedAt().format(FORMATTER));
        return dto;
    }
}
