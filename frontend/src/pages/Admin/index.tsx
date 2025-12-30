import { useEffect, useState } from 'react';
import {
  Card,
  Tabs,
  Table,
  Tag,
  Button,
  Modal,
  Form,
  Input,
  message,
  Space,
  Statistic,
  Row,
  Col,
} from 'antd';
import {
  UserOutlined,
  BookOutlined,
  StopOutlined,
  CheckCircleOutlined,
} from '@ant-design/icons';
import dayjs from 'dayjs';
import {
  getAllUsers,
  getAllBookings,
  getStatistics,
  addBlacklist,
  getBlacklist,
  removeBlacklist,
} from '../../services/api';
import type { User, Booking, Blacklist, Statistics } from '../../types';
import './style.css';

export default function Admin() {
  const [users, setUsers] = useState<User[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [blacklist, setBlacklist] = useState<Blacklist[]>([]);
  const [statistics, setStatistics] = useState<Statistics | null>(null);
  const [loading, setLoading] = useState(false);
  const [blacklistModalVisible, setBlacklistModalVisible] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [form] = Form.useForm();

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [usersRes, bookingsRes, blacklistRes, statsRes] = await Promise.all([
        getAllUsers(),
        getAllBookings(),
        getBlacklist(),
        getStatistics(),
      ]);

      if (usersRes.success) setUsers(usersRes.data);
      if (bookingsRes.success) setBookings(bookingsRes.data);
      if (blacklistRes.success) setBlacklist(blacklistRes.data);
      if (statsRes.success) setStatistics(statsRes.data);
    } catch (error) {
      console.error('加载数据失败:', error);
    } finally {
      setLoading(false);
    }
  };


  // 打开黑名单对话框
  const handleOpenBlacklistModal = (userId: number) => {
    setSelectedUserId(userId);
    setBlacklistModalVisible(true);
    form.resetFields();
  };

  // 添加黑名单
  const handleAddBlacklist = async () => {
    try {
      const values = await form.validateFields();
      if (selectedUserId) {
        await addBlacklist(selectedUserId, values.reason);
        message.success('已加入黑名单');
        setBlacklistModalVisible(false);
        loadData();
        form.resetFields();
      }
    } catch (error) {
      console.error('添加黑名单失败:', error);
    }
  };

  // 移除黑名单
  const handleRemoveBlacklist = async (id: number) => {
    try {
      await removeBlacklist(id);
      message.success('已移除黑名单');
      loadData();
    } catch (error) {
      console.error('移除黑名单失败:', error);
    }
  };

  // 检查用户是否在黑名单中
  const isUserBlacklisted = (userId: number) => {
    return blacklist.some(b => b.userId === userId);
  };

  // 获取用户的黑名单信息
  const getUserBlacklistInfo = (userId: number) => {
    return blacklist.find(b => b.userId === userId);
  };

  // 用户表格列
  const userColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { 
      title: '用户名', 
      dataIndex: 'username', 
      key: 'username',
    },
    { title: '姓名', dataIndex: 'name', key: 'name' },
    { title: '学号', dataIndex: 'studentId', key: 'studentId' },
    { title: '手机', dataIndex: 'phone', key: 'phone' },
    {
      title: '角色',
      dataIndex: 'role',
      key: 'role',
      render: (role: string) => (
        <Tag color={role === 'ADMIN' ? 'red' : 'blue'}>
          {role === 'ADMIN' ? '管理员' : '用户'}
        </Tag>
      ),
    },
    {
      title: '黑名单状态',
      key: 'blacklistStatus',
      render: (_: any, record: User) => {
        const blacklistInfo = getUserBlacklistInfo(record.id);
        if (blacklistInfo) {
          return (
            <div>
              <Tag color="red">🚫 已拉黑</Tag>
              <div style={{ fontSize: '12px', color: '#666', marginTop: 4 }}>
                原因：{blacklistInfo.reason}
              </div>
              <div style={{ fontSize: '12px', color: '#999', marginTop: 2 }}>
                时间：{blacklistInfo.createdTime}
              </div>
            </div>
          );
        }
        return <Tag color="success">✅ 正常</Tag>;
      },
    },
    { title: '违约次数', dataIndex: 'violationCount', key: 'violationCount' },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: User) => {
        const isBlacklisted = isUserBlacklisted(record.id);
        return (
          <Space>
            {!isBlacklisted ? (
              <Button
                size="small"
                danger
                onClick={() => handleOpenBlacklistModal(record.id)}
              >
                拉黑
              </Button>
            ) : (
              <Button
                size="small"
                type="primary"
                onClick={() => {
                  const blacklistInfo = getUserBlacklistInfo(record.id);
                  if (blacklistInfo) {
                    handleRemoveBlacklist(blacklistInfo.id);
                  }
                }}
              >
                解除拉黑
              </Button>
            )}
          </Space>
        );
      },
    },
  ];

  // 预约表格列
  const bookingColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '用户', dataIndex: 'username', key: 'username' },
    { title: '教学楼', dataIndex: 'buildingName', key: 'buildingName' },
    { title: '教室', dataIndex: 'classroomName', key: 'classroomName' },
    { title: '座位号', dataIndex: 'seatNumber', key: 'seatNumber' },
    {
      title: '开始时间',
      dataIndex: 'startTime',
      key: 'startTime',
      render: (time: string) => dayjs(time).format('YYYY-MM-DD HH:mm'),
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
      key: 'endTime',
      render: (time: string) => dayjs(time).format('HH:mm'),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const statusMap: Record<string, { color: string; text: string }> = {
          PENDING: { color: 'warning', text: '待签到' },
          ACTIVE: { color: 'processing', text: '使用中' },
          CANCELLED: { color: 'default', text: '已取消' },
          COMPLETED: { color: 'success', text: '已完成' },
          VIOLATED: { color: 'error', text: '违约' },
          TIMEOUT: { color: 'error', text: '签到超时' },
        };
        const config = statusMap[status] || { color: 'default', text: status };
        return <Tag color={config.color}>{config.text}</Tag>;
      },
    },
  ];

  // 黑名单表格列
  const blacklistColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '姓名', dataIndex: 'realName', key: 'realName' },
    { 
      title: '拉黑原因', 
      dataIndex: 'reason', 
      key: 'reason',
      render: (text: string) => (
        <span style={{ color: '#ff4d4f' }}>{text}</span>
      ),
    },
    { title: '操作人', dataIndex: 'createdByUsername', key: 'createdByUsername' },
    {
      title: '拉黑时间',
      dataIndex: 'createdTime',
      key: 'createdTime',
    },
    {
      title: '状态',
      key: 'blacklistStatus',
      render: () => (
        <Tag color="error">🚫 永久生效</Tag>
      ),
    },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: Blacklist) => (
        <Button
          size="small"
          type="primary"
          onClick={() => handleRemoveBlacklist(record.id)}
        >
          解除拉黑
        </Button>
      ),
    },
  ];

  const items = [
    {
      key: 'users',
      label: (
        <span>
          <UserOutlined /> 用户管理
        </span>
      ),
      children: (
        <Table
          columns={userColumns}
          dataSource={users}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1200 }}
        />
      ),
    },
    {
      key: 'bookings',
      label: (
        <span>
          <BookOutlined /> 预约管理
        </span>
      ),
      children: (
        <Table
          columns={bookingColumns}
          dataSource={bookings}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1200 }}
        />
      ),
    },
    {
      key: 'blacklist',
      label: (
        <span>
          <StopOutlined /> 黑名单管理
        </span>
      ),
      children: (
        <Table
          columns={blacklistColumns}
          dataSource={blacklist}
          rowKey="id"
          loading={loading}
        />
      ),
    },
  ];

  return (
    <div className="admin-container">
      <h1>系统管理</h1>

      {statistics && (
        <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
          <Col xs={24} sm={12} lg={6}>
            <Card>
              <Statistic
                title="总用户数"
                value={statistics.totalUsers}
                prefix={<UserOutlined />}
              />
            </Card>
          </Col>
          <Col xs={24} sm={12} lg={6}>
            <Card>
              <Statistic
                title="总预约数"
                value={statistics.totalBookings}
                prefix={<BookOutlined />}
              />
            </Card>
          </Col>
          <Col xs={24} sm={12} lg={6}>
            <Card>
              <Statistic
                title="今日预约"
                value={statistics.todayBookings}
                prefix={<CheckCircleOutlined />}
              />
            </Card>
          </Col>
          <Col xs={24} sm={12} lg={6}>
            <Card>
              <Statistic
                title="当前有效预约"
                value={statistics.activeBookings}
                prefix={<BookOutlined />}
              />
            </Card>
          </Col>
        </Row>
      )}

      <Card>
        <Tabs items={items} />
      </Card>

      <Modal
        title="添加到黑名单"
        open={blacklistModalVisible}
        onOk={handleAddBlacklist}
        onCancel={() => setBlacklistModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="拉黑原因"
            name="reason"
            rules={[{ required: true, message: '请输入拉黑原因' }]}
          >
            <Input.TextArea rows={3} placeholder="请输入拉黑原因" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
