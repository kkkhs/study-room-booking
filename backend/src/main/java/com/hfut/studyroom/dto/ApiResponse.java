package com.hfut.studyroom.dto;

import lombok.Data;

/**
 * 统一API响应格式
 */
@Data
public class ApiResponse<T> {
    
    private Boolean success;
    private Integer code;
    private String message;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return error(400, message);
    }
}
