import { useEffect, useState } from 'react';
import { Card, Row, Col, Statistic, List, Tag, Button, Empty, Spin } from 'antd';
import {
  BookOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
  EnvironmentOutlined,
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import { getMyBookings, cancelBooking, checkIn } from '../../services/api';
import { getUser } from '../../utils/auth';
import CheckInGuide from '../../components/CheckInGuide';
import type { Booking } from '../../types';
import './style.css';

export default function Home() {
  const navigate = useNavigate();
  const user = getUser();
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState<number | null>(null);

  // 加载预约数据
  const loadBookings = async () => {
    setLoading(true);
    try {
      const response = await getMyBookings();
      if (response.success) {
        setBookings(response.data);
      }
    } catch (error) {
      console.error('加载预约失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBookings();
  }, []);

  // 取消预约
  const handleCancel = async (id: number) => {
    setActionLoading(id);
    try {
      const response = await cancelBooking(id);
      if (response.success) {
        await loadBookings();
      }
    } catch (error) {
      console.error('取消预约失败:', error);
    } finally {
      setActionLoading(null);
    }
  };

  // 签到
  const handleCheckIn = async (id: number) => {
    setActionLoading(id);
    try {
      const response = await checkIn(id);
      if (response.success) {
        await loadBookings();
      }
    } catch (error) {
      console.error('签到失败:', error);
    } finally {
      setActionLoading(null);
    }
  };

  // 获取状态标签
  const getStatusTag = (status: string) => {
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
  };

  // 统计数据
  const activeBookings = bookings.filter(b => b.status === 'ACTIVE' || b.status === 'PENDING').length;
  const todayBookings = bookings.filter(b => 
    dayjs(b.startTime).format('YYYY-MM-DD') === dayjs().format('YYYY-MM-DD')
  ).length;

  // 检查是否有待签到的预约
  const hasPendingBookings = bookings.some(b => b.status === 'PENDING');

  return (
    <div className="home-container">
      <div className="welcome-section">
        <h1>欢迎回来，{user?.name || user?.username}！</h1>
        <p>开始您的学习之旅吧 📖</p>
      </div>

      {hasPendingBookings && <CheckInGuide />}

      <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
        <Col xs={24} sm={12} lg={8}>
          <Card>
            <Statistic
              title="总预约次数"
              value={bookings.length}
              prefix={<BookOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={8}>
          <Card>
            <Statistic
              title="当前有效预约"
              value={activeBookings}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={8}>
          <Card>
            <Statistic
              title="今日预约"
              value={todayBookings}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
      </Row>

      <Card 
        title="我的预约" 
        extra={
          <Button type="primary" onClick={() => navigate('/booking')}>
            新建预约
          </Button>
        }
      >
        <Spin spinning={loading}>
          {bookings.length === 0 ? (
            <Empty description="暂无预约记录">
              <Button type="primary" onClick={() => navigate('/booking')}>
                立即预约
              </Button>
            </Empty>
          ) : (
            <List
              dataSource={bookings}
              renderItem={(booking) => {
                // 判断是否可以签到（预约开始前30分钟到预约开始后15分钟）
                const now = dayjs();
                const startTime = dayjs(booking.startTime);
                const canCheckIn = booking.status === 'PENDING' && 
                  now.isAfter(startTime.subtract(30, 'minute')) &&
                  now.isBefore(startTime.add(15, 'minute'));
                
                // 生成签到提示文本
                const getCheckInHint = () => {
                  if (booking.status !== 'PENDING') return null;
                  
                  const timeDiff = startTime.diff(now, 'minute');
                  if (timeDiff > 30) {
                    return `（${Math.floor(timeDiff / 60)}小时${timeDiff % 60}分钟后可签到）`;
                  } else if (timeDiff > 0) {
                    return `（${timeDiff}分钟后开始，可提前签到）`;
                  } else if (timeDiff >= -15) {
                    return '（可以签到）';
                  } else {
                    return '（签到已超时）';
                  }
                };

                return (
                  <List.Item
                    actions={
                      booking.status === 'PENDING' 
                        ? [
                            <Button 
                              key="checkin" 
                              type="primary" 
                              size="small"
                              loading={actionLoading === booking.id}
                              onClick={() => handleCheckIn(booking.id)}
                              disabled={!canCheckIn}
                            >
                              签到
                            </Button>,
                            <Button 
                              key="cancel" 
                              danger 
                              size="small"
                              loading={actionLoading === booking.id}
                              onClick={() => handleCancel(booking.id)}
                            >
                              取消
                            </Button>
                          ]
                        : []
                    }
                  >
                    <List.Item.Meta
                      title={
                        <div>
                          <EnvironmentOutlined style={{ marginRight: 8 }} />
                          {booking.buildingName} - {booking.classroomName} - 座位 {booking.seatNumber}
                          <span style={{ marginLeft: 16 }}>
                            {getStatusTag(booking.status)}
                          </span>
                        </div>
                      }
                      description={
                        <div>
                          <div>
                            <ClockCircleOutlined style={{ marginRight: 8 }} />
                            {dayjs(booking.startTime).format('YYYY-MM-DD HH:mm')} 至{' '}
                            {dayjs(booking.endTime).format('HH:mm')}
                            {booking.status === 'PENDING' && (
                              <span style={{ marginLeft: 8, color: '#1890ff', fontSize: '12px' }}>
                                {getCheckInHint()}
                              </span>
                            )}
                          </div>
                          {booking.status === 'PENDING' && (
                            <div style={{ marginTop: 4, fontSize: '12px', color: '#999' }}>
                              💡 签到时间：预约开始前30分钟至预约开始后15分钟
                            </div>
                          )}
                        </div>
                      }
                    />
                  </List.Item>
                );
              }}
            />
          )}
        </Spin>
      </Card>
    </div>
  );
}
