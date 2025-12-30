import { useEffect, useState } from 'react';
import { 
  Card, 
  Form, 
  Select, 
  DatePicker, 
  TimePicker, 
  Button, 
  Row, 
  Col, 
  message, 
  Spin,
  Empty,
  Steps,
} from 'antd';
import {
  HomeOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
} from '@ant-design/icons';
import dayjs, { Dayjs } from 'dayjs';
import {
  getBuildings,
  getClassroomsByBuilding,
  createBooking,
} from '../../services/api';
import request from '../../utils/request';
import SeatMap from '../../components/SeatMap';
import type { Building, Classroom, Seat } from '../../types';
import './style.css';

export default function Booking() {
  const [form] = Form.useForm();
  const [buildings, setBuildings] = useState<Building[]>([]);
  const [classrooms, setClassrooms] = useState<Classroom[]>([]);
  const [seats, setSeats] = useState<Seat[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchLoading, setSearchLoading] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);

  // 加载教学楼
  useEffect(() => {
    loadBuildings();
  }, []);

  const loadBuildings = async () => {
    setLoading(true);
    try {
      const response = await getBuildings();
      if (response.success) {
        setBuildings(response.data);
      }
    } catch (error) {
      console.error('加载教学楼失败:', error);
    } finally {
      setLoading(false);
    }
  };

  // 教学楼改变时加载教室
  const handleBuildingChange = async (buildingId: number) => {
    form.setFieldsValue({ classroomId: undefined });
    setClassrooms([]);
    setSeats([]);
    setCurrentStep(0);

    try {
      console.log('正在加载教室，教学楼ID:', buildingId);
      const response = await getClassroomsByBuilding(buildingId);
      console.log('教室API响应:', response);
      
      if (response.success && response.data) {
        setClassrooms(response.data);
        message.success(`已加载 ${response.data.length} 个教室`);
      } else {
        message.error('加载教室失败');
      }
    } catch (error) {
      console.error('加载教室失败:', error);
      message.error('加载教室失败，请重试');
    }
  };

  // 选择教室后加载座位
  const handleClassroomChange = async () => {
    setSeats([]);
    setCurrentStep(1);
  };

  // 查看座位布局
  const handleViewSeats = async () => {
    try {
      await form.validateFields();
      const values = form.getFieldsValue();

      if (!values.classroomId) {
        message.warning('请先选择教室');
        return;
      }

      setSearchLoading(true);
      
      // 构建查询参数
      const date = dayjs(values.date).format('YYYY-MM-DD');
      const startTime = dayjs(values.startTime).format('HH:mm:ss');
      const endTime = dayjs(values.endTime).format('HH:mm:ss');
      
      const url = `/classrooms/${values.classroomId}/seats?date=${date}&startTime=${startTime}&endTime=${endTime}`;
      const response = await request.get<any, any>(url);
      
      if (response && response.data) {
        setSeats(response.data);
        setCurrentStep(2);
        const available = response.data.filter((s: Seat) => s.status === 'AVAILABLE').length;
        message.success(`找到 ${available} 个可用座位`);
      }
    } catch (error) {
      console.error('加载座位失败:', error);
    } finally {
      setSearchLoading(false);
    }
  };

  // 选择座位并预约
  const handleSelectSeat = async (seat: Seat) => {
    const values = form.getFieldsValue();
    const startTime = dayjs(values.date)
      .hour(dayjs(values.startTime).hour())
      .minute(dayjs(values.startTime).minute())
      .second(0);
    const endTime = dayjs(values.date)
      .hour(dayjs(values.endTime).hour())
      .minute(dayjs(values.endTime).minute())
      .second(0);

    try {
      setLoading(true);
      const response = await createBooking({
        seatId: seat.id,
        startTime: startTime.format('YYYY-MM-DD HH:mm:ss'),
        endTime: endTime.format('YYYY-MM-DD HH:mm:ss'),
      });

      if (response.success) {
        message.success('预约成功！请在签到窗口内完成签到');
        // 刷新座位状态
        handleViewSeats();
      } else {
        message.error(response.message || '预约失败');
      }
    } catch (error: any) {
      console.error('预约失败:', error);
      // 错误已经在 request 拦截器中显示了，这里不需要重复显示
      // 只在网络错误等特殊情况下才兜底显示
      if (!error?.response) {
        message.error('网络错误，请检查网络连接');
      }
      // 其他错误（包括业务错误）已在拦截器中处理，不再重复显示
    } finally {
      setLoading(false);
    }
  };

  // 禁用过去的日期
  const disabledDate = (current: Dayjs) => {
    return current && current < dayjs().startOf('day');
  };

  // 获取下一个整点小时作为默认开始时间
  const getNextHour = () => {
    const now = dayjs();
    const nextHour = now.add(1, 'hour').startOf('hour');
    return nextHour;
  };

  // 禁用过去的时间
  const disabledTime = () => {
    const selectedDate = form.getFieldValue('date');
    const isToday = selectedDate && dayjs(selectedDate).isSame(dayjs(), 'day');
    
    if (!isToday) {
      return {}; // 如果不是今天，不禁用任何时间
    }

    const now = dayjs();
    const currentHour = now.hour();
    const currentMinute = now.minute();

    return {
      disabledHours: () => {
        // 禁用当前小时之前的所有小时
        return Array.from({ length: currentHour }, (_, i) => i);
      },
      disabledMinutes: (selectedHour: number) => {
        // 如果选择的是当前小时，禁用当前分钟之前的分钟
        if (selectedHour === currentHour) {
          return Array.from({ length: 60 }, (_, i) => i).filter(m => m < currentMinute);
        }
        return [];
      },
    };
  };

  // 结束时间禁用规则
  const disabledEndTime = () => {
    const startTime = form.getFieldValue('startTime');
    const selectedDate = form.getFieldValue('date');
    const isToday = selectedDate && dayjs(selectedDate).isSame(dayjs(), 'day');
    
    if (!startTime) {
      return disabledTime(); // 如果没有选择开始时间，使用相同的规则
    }

    const startHour = dayjs(startTime).hour();
    const startMinute = dayjs(startTime).minute();
    const now = dayjs();
    const currentHour = now.hour();
    const currentMinute = now.minute();

    return {
      disabledHours: () => {
        const hours = [];
        // 禁用开始时间之前的小时
        for (let i = 0; i <= startHour; i++) {
          hours.push(i);
        }
        // 如果是今天，还要禁用当前时间之前的小时
        if (isToday) {
          for (let i = 0; i < currentHour; i++) {
            if (!hours.includes(i)) {
              hours.push(i);
            }
          }
        }
        return hours;
      },
      disabledMinutes: (selectedHour: number) => {
        // 如果选择的是开始时间的小时，禁用开始时间分钟及之前的分钟
        if (selectedHour === startHour) {
          return Array.from({ length: 60 }, (_, i) => i).filter(m => m <= startMinute);
        }
        // 如果是今天且选择的是当前小时，禁用当前分钟之前的分钟
        if (isToday && selectedHour === currentHour) {
          return Array.from({ length: 60 }, (_, i) => i).filter(m => m < currentMinute);
        }
        return [];
      },
    };
  };

  return (
    <div className="booking-container">
      <Steps
        current={currentStep}
        items={[
          { title: '选择教室', icon: <HomeOutlined /> },
          { title: '设置时间', icon: <ClockCircleOutlined /> },
          { title: '选择座位', icon: <CheckCircleOutlined /> },
        ]}
        style={{ marginBottom: 24 }}
      />

      <Card title="🏫 选择教室和时间" className="search-card">
        <Form
          form={form}
          layout="vertical"
          initialValues={{
            date: dayjs(),
            startTime: getNextHour(),
            endTime: getNextHour().add(2, 'hour'),
          }}
        >
          <Row gutter={16}>
            <Col xs={24} sm={12} lg={6}>
              <Form.Item
                label="教学楼"
                name="buildingId"
                rules={[{ required: true, message: '请选择教学楼' }]}
              >
                <Select
                  placeholder="请选择教学楼"
                  onChange={handleBuildingChange}
                  loading={loading}
                  size="large"
                >
                  {buildings.map(building => (
                    <Select.Option key={building.id} value={building.id}>
                      <HomeOutlined /> {building.name}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>

            <Col xs={24} sm={12} lg={6}>
              <Form.Item 
                label="教室" 
                name="classroomId"
                rules={[{ required: true, message: '请选择教室' }]}
              >
                <Select 
                  placeholder="请选择教室" 
                  onChange={handleClassroomChange}
                  size="large"
                  disabled={classrooms.length === 0}
                >
                  {classrooms.map(classroom => (
                    <Select.Option key={classroom.id} value={classroom.id}>
                      {classroom.roomNumber}教室 (容量{classroom.capacity})
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>

            <Col xs={24} sm={12} lg={4}>
              <Form.Item
                label="日期"
                name="date"
                rules={[{ required: true, message: '请选择日期' }]}
              >
                <DatePicker 
                  style={{ width: '100%' }} 
                  disabledDate={disabledDate}
                  size="large"
                />
              </Form.Item>
            </Col>

            <Col xs={24} sm={12} lg={4}>
              <Form.Item
                label="开始时间"
                name="startTime"
                rules={[{ required: true, message: '请选择开始时间' }]}
              >
                <TimePicker 
                  format="HH:mm" 
                  style={{ width: '100%' }} 
                  size="large"
                  minuteStep={30}
                  disabledTime={disabledTime}
                  showNow={false}
                  onChange={() => {
                    // 当开始时间改变时，清空结束时间，让用户重新选择
                    form.setFieldValue('endTime', null);
                  }}
                />
              </Form.Item>
            </Col>

            <Col xs={24} sm={12} lg={4}>
              <Form.Item
                label="结束时间"
                name="endTime"
                rules={[{ required: true, message: '请选择结束时间' }]}
              >
                <TimePicker 
                  format="HH:mm" 
                  style={{ width: '100%' }} 
                  size="large"
                  minuteStep={30}
                  disabledTime={disabledEndTime}
                  showNow={false}
                />
              </Form.Item>
            </Col>
          </Row>

          <Row>
            <Col span={24}>
              <Button 
                type="primary" 
                onClick={handleViewSeats}
                loading={searchLoading}
                size="large"
                block
                style={{ maxWidth: 300 }}
              >
                查看座位布局
              </Button>
            </Col>
          </Row>
        </Form>
      </Card>

      {seats.length > 0 && (
        <div style={{ marginTop: 24 }}>
          <Spin spinning={loading}>
            <SeatMap seats={seats} onSelect={handleSelectSeat} />
          </Spin>
        </div>
      )}

      {seats.length === 0 && currentStep === 2 && (
        <Card style={{ marginTop: 24 }}>
          <Empty description="该教室暂无可用座位" />
        </Card>
      )}
    </div>
  );
}
