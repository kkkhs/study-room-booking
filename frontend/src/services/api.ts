import request from '../utils/request';
import type {
  ApiResponse,
  User,
  Building,
  Classroom,
  Seat,
  Booking,
  Blacklist,
  LoginRequest,
  RegisterRequest,
  BookingRequest,
  SeatQueryParams,
  Statistics,
} from '../types';

// ==================== 认证相关 ====================

// 用户登录
export const login = (data: LoginRequest) => {
  return request.post<any, ApiResponse<{ token: string; user: User }>>('/auth/login', data);
};

// 用户注册
export const register = (data: RegisterRequest) => {
  return request.post<any, ApiResponse<User>>('/auth/register', data);
};

// 获取当前用户信息
export const getCurrentUser = () => {
  return request.get<any, ApiResponse<User>>('/auth/me');
};

// ==================== 教学楼相关 ====================

// 获取所有教学楼
export const getBuildings = () => {
  return request.get<any, ApiResponse<Building[]>>('/buildings');
};

// 获取教学楼详情
export const getBuilding = (id: number) => {
  return request.get<any, ApiResponse<Building>>(`/buildings/${id}`);
};

// ==================== 教室相关 ====================

// 获取所有教室
export const getClassrooms = () => {
  return request.get<any, ApiResponse<Classroom[]>>('/classrooms');
};

// 根据教学楼获取教室
export const getClassroomsByBuilding = (buildingId: number) => {
  return request.get<any, ApiResponse<Classroom[]>>(`/classrooms?buildingId=${buildingId}`);
};

// 获取教室详情
export const getClassroom = (id: number) => {
  return request.get<any, ApiResponse<Classroom>>(`/classrooms/${id}`);
};

// ==================== 座位相关 ====================

// 查询可用座位
export const getAvailableSeats = (params: SeatQueryParams) => {
  return request.get<any, ApiResponse<Seat[]>>('/seats/available', { params });
};

// 根据教室获取座位（包含已预约的座位状态）
export const getSeatsByClassroom = (classroomId: number) => {
  return request.get<any, ApiResponse<Seat[]>>(`/classrooms/${classroomId}/seats`);
};

// 获取座位详情
export const getSeat = (id: number) => {
  return request.get<any, ApiResponse<Seat>>(`/seats/${id}`);
};

// ==================== 预约相关 ====================

// 创建预约
export const createBooking = (data: BookingRequest) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return request.post<any, ApiResponse<Booking>>(`/bookings?userId=${user.id}`, data);
};

// 获取我的预约
export const getMyBookings = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return request.get<any, ApiResponse<Booking[]>>(`/bookings/my?userId=${user.id}`);
};

// 取消预约
export const cancelBooking = (id: number) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return request.post<any, ApiResponse<void>>(`/bookings/${id}/cancel?userId=${user.id}`);
};

// 签到
export const checkIn = (id: number) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return request.post<any, ApiResponse<void>>(`/bookings/${id}/checkin?userId=${user.id}`);
};

// ==================== 管理员相关 ====================

// 获取所有用户
export const getAllUsers = () => {
  return request.get<any, ApiResponse<User[]>>('/admin/users');
};

// 获取所有预约
export const getAllBookings = () => {
  return request.get<any, ApiResponse<Booking[]>>('/admin/bookings');
};

// 获取统计数据
export const getStatistics = () => {
  return request.get<any, ApiResponse<Statistics>>('/admin/statistics');
};

// 禁用用户
export const disableUser = (userId: number) => {
  return request.put<any, ApiResponse<any>>(`/admin/users/${userId}?status=DISABLED`);
};

// 启用用户
export const enableUser = (userId: number) => {
  return request.put<any, ApiResponse<any>>(`/admin/users/${userId}?status=ACTIVE`);
};

// 添加黑名单
export const addBlacklist = (userId: number, reason: string) => {
  const admin = JSON.parse(localStorage.getItem('user') || '{}');
  return request.post<any, ApiResponse<Blacklist>>(
    `/admin/blacklist?userId=${userId}&reason=${encodeURIComponent(reason)}&adminId=${admin.id}`
  );
};

// 获取黑名单
export const getBlacklist = () => {
  return request.get<any, ApiResponse<Blacklist[]>>('/admin/blacklist');
};

// 移除黑名单
export const removeBlacklist = (id: number) => {
  return request.delete<any, ApiResponse<void>>(`/admin/blacklist/${id}`);
};
