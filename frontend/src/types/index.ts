// API 响应类型
export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data: T;
}

// 用户类型
export interface User {
  id: number;
  username: string;
  name: string;
  studentId: string;
  phone: string;
  email: string;
  role: 'USER' | 'ADMIN';
  status: 'ACTIVE' | 'DISABLED';
  violationCount: number;
  createdTime: string;
  lastLoginTime: string;
}

// 教学楼类型
export interface Building {
  id: number;
  name: string;
  location: string;
  floors: number;
  status: 'OPEN' | 'CLOSED' | 'MAINTENANCE';
  openTime: string;
  closeTime: string;
}

// 教室类型
export interface Classroom {
  id: number;
  buildingId: number;
  buildingName?: string;
  name?: string;
  roomNumber: string;
  floor: number;
  capacity: number;
  availableSeats?: number;
  status: 'OPEN' | 'CLOSED' | 'MAINTENANCE';
}

// 座位类型
export interface Seat {
  id: number;
  classroomId: number;
  classroomName: string;
  buildingName: string;
  seatNumber: string;
  location: string;
  status: 'AVAILABLE' | 'OCCUPIED' | 'DISABLED';
  rowNum: number;
  colNum: number;
  hasSocket?: boolean;
  nearWindow?: boolean;
}

// 预约类型
export interface Booking {
  id: number;
  userId: number;
  username: string;
  seatId: number;
  seatNumber: string;
  classroomName: string;
  buildingName: string;
  startTime: string;
  endTime: string;
  status: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'COMPLETED' | 'VIOLATED' | 'TIMEOUT';
  createdTime: string;
}

// 黑名单类型
export interface Blacklist {
  id: number;
  userId: number;
  username: string;
  realName: string;
  reason: string;
  createdByUsername?: string;
  createdTime: string;
}

// 登录请求
export interface LoginRequest {
  username: string;
  password: string;
}

// 注册请求
export interface RegisterRequest {
  username: string;
  password: string;
  name: string;
  studentId: string;
  phone: string;
  email: string;
}

// 预约请求
export interface BookingRequest {
  seatId: number;
  startTime: string;
  endTime: string;
}

// 座位查询参数
export interface SeatQueryParams {
  buildingId?: number;
  classroomId?: number;
  date?: string;
  startTime?: string;
  endTime?: string;
  hasSocket?: boolean;
  nearWindow?: boolean;
}

// 统计数据
export interface Statistics {
  totalUsers: number;
  totalBookings: number;
  todayBookings: number;
  activeBookings: number;
}
