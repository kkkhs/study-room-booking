import { useState } from 'react';
import { Form, Input, Button, Card, message, Tabs } from 'antd';
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, IdcardOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { login, register } from '../../services/api';
import { saveToken, saveUser } from '../../utils/auth';
import type { LoginRequest, RegisterRequest } from '../../types';
import './style.css';

export default function Login() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('login');

  // 处理登录
  const handleLogin = async (values: LoginRequest) => {
    setLoading(true);
    try {
      const response = await login(values);
      if (response.success) {
        saveToken(response.data.token);
        saveUser(response.data.user);
        message.success('登录成功！');
        navigate('/home');
      } else {
        message.error(response.message || '登录失败');
      }
    } catch (error) {
      message.error('登录失败，请检查用户名和密码');
    } finally {
      setLoading(false);
    }
  };

  // 处理注册
  const handleRegister = async (values: RegisterRequest) => {
    setLoading(true);
    try {
      const response = await register(values);
      if (response.success) {
        message.success('注册成功！请登录');
        setActiveTab('login');
      } else {
        message.error(response.message || '注册失败');
      }
    } catch (error) {
      message.error('注册失败，请检查输入信息');
    } finally {
      setLoading(false);
    }
  };

  const loginForm = (
    <Form
      name="login"
      onFinish={handleLogin}
      autoComplete="off"
      size="large"
    >
      <Form.Item
        name="username"
        rules={[{ required: true, message: '请输入用户名！' }]}
      >
        <Input prefix={<UserOutlined />} placeholder="用户名" />
      </Form.Item>

      <Form.Item
        name="password"
        rules={[{ required: true, message: '请输入密码！' }]}
      >
        <Input.Password prefix={<LockOutlined />} placeholder="密码" />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading} block>
          登录
        </Button>
      </Form.Item>

      <div style={{ textAlign: 'center', color: '#999' }}>
        <p>测试账号：admin / admin123 (管理员)</p>
        <p>user1 / password123 (普通用户)</p>
      </div>
    </Form>
  );

  const registerForm = (
    <Form
      name="register"
      onFinish={handleRegister}
      autoComplete="off"
      size="large"
    >
      <Form.Item
        name="username"
        rules={[
          { required: true, message: '请输入用户名！' },
          { min: 3, message: '用户名至少3个字符！' },
        ]}
      >
        <Input prefix={<UserOutlined />} placeholder="用户名" />
      </Form.Item>

      <Form.Item
        name="password"
        rules={[
          { required: true, message: '请输入密码！' },
          { min: 6, message: '密码至少6个字符！' },
        ]}
      >
        <Input.Password prefix={<LockOutlined />} placeholder="密码" />
      </Form.Item>

      <Form.Item
        name="name"
        rules={[{ required: true, message: '请输入姓名！' }]}
      >
        <Input prefix={<UserOutlined />} placeholder="姓名" />
      </Form.Item>

      <Form.Item
        name="studentId"
        rules={[{ required: true, message: '请输入学号！' }]}
      >
        <Input prefix={<IdcardOutlined />} placeholder="学号" />
      </Form.Item>

      <Form.Item
        name="phone"
        rules={[
          { required: true, message: '请输入手机号！' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号！' },
        ]}
      >
        <Input prefix={<PhoneOutlined />} placeholder="手机号" />
      </Form.Item>

      <Form.Item
        name="email"
        rules={[
          { required: true, message: '请输入邮箱！' },
          { type: 'email', message: '请输入正确的邮箱！' },
        ]}
      >
        <Input prefix={<MailOutlined />} placeholder="邮箱" />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading} block>
          注册
        </Button>
      </Form.Item>
    </Form>
  );

  const items = [
    { key: 'login', label: '登录', children: loginForm },
    { key: 'register', label: '注册', children: registerForm },
  ];

  return (
    <div className="login-container">
      <div className="login-background" />
      <Card 
        className="login-card"
        bordered={false}
      >
        <div className="login-header">
          <h1>📚 自习室预约系统</h1>
          <p>合肥工业大学宣城校区</p>
        </div>
        <Tabs 
          activeKey={activeTab} 
          onChange={setActiveTab} 
          items={items}
          centered
        />
      </Card>
    </div>
  );
}
