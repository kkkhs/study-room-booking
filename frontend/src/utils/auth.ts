import type { User } from '../types';

// 保存用户信息
export const saveUser = (user: User) => {
  localStorage.setItem('user', JSON.stringify(user));
};

// 获取用户信息
export const getUser = (): User | null => {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
};

// 保存 token
export const saveToken = (token: string) => {
  localStorage.setItem('token', token);
};

// 获取 token
export const getToken = (): string | null => {
  return localStorage.getItem('token');
};

// 清除用户信息
export const clearAuth = () => {
  localStorage.removeItem('user');
  localStorage.removeItem('token');
};

// 检查是否已登录
export const isAuthenticated = (): boolean => {
  return !!getToken();
};

// 检查是否是管理员
export const isAdmin = (): boolean => {
  const user = getUser();
  return user?.role === 'ADMIN';
};
