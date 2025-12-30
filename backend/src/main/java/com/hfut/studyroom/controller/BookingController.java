package com.hfut.studyroom.controller;

import com.hfut.studyroom.dto.ApiResponse;
import com.hfut.studyroom.dto.BookingDTO;
import com.hfut.studyroom.dto.BookingRequest;
import com.hfut.studyroom.entity.Booking;
import com.hfut.studyroom.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约控制器
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    
    private final BookingService bookingService;
    
    /**
     * 创建预约
     * 注意：这里简化处理，实际应该从登录session中获取userId
     */
    @PostMapping
    public ApiResponse<BookingDTO> createBooking(
            @RequestParam Long userId,
            @Valid @RequestBody BookingRequest request
    ) {
        Booking booking = bookingService.createBooking(userId, request);
        return ApiResponse.success("预约成功，请在15分钟内签到", BookingDTO.fromEntity(booking));
    }
    
    /**
     * 获取我的预约列表
     */
    @GetMapping("/my")
    public ApiResponse<List<BookingDTO>> getMyBookings(@RequestParam Long userId) {
        List<Booking> bookings = bookingService.getMyBookings(userId);
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(bookingDTOs);
    }
    
    /**
     * 签到
     */
    @PostMapping("/{id}/checkin")
    public ApiResponse<BookingDTO> checkIn(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        Booking booking = bookingService.checkIn(id, userId);
        return ApiResponse.success("签到成功", BookingDTO.fromEntity(booking));
    }
    
    /**
     * 签退
     */
    @PostMapping("/{id}/checkout")
    public ApiResponse<BookingDTO> checkOut(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        Booking booking = bookingService.checkOut(id, userId);
        return ApiResponse.success("签退成功", BookingDTO.fromEntity(booking));
    }
    
    /**
     * 取消预约
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<BookingDTO> cancelBooking(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        Booking booking = bookingService.cancelBooking(id, userId);
        return ApiResponse.success("取消成功", BookingDTO.fromEntity(booking));
    }
}
