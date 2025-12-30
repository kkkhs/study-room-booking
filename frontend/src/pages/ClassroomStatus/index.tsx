import { useEffect, useState } from 'react';
import { Card, Select, DatePicker, Table, Tag, Spin, Alert, Empty } from 'antd';
import { EnvironmentOutlined, ClockCircleOutlined } from '@ant-design/icons';
import dayjs, { Dayjs } from 'dayjs';
import { getBuildings } from '../../services/api';
import request from '../../utils/request';
import type { Building } from '../../types';
import './style.css';

interface ClassroomOccupancy {
  id: number;
  classroomId: number;
  classroomName: string;
  buildingName: string;
  occupancyDate: string;
  startTime: string;
  endTime: string;
  reason: string;
  type: string;
  occupiedBy: string;
  status: string;
}

export default function ClassroomStatus() {
  const [buildings, setBuildings] = useState<Building[]>([]);
  const [selectedBuilding, setSelectedBuilding] = useState<number>();
  const [selectedDate, setSelectedDate] = useState<Dayjs>(dayjs());
  const [occupancies, setOccupancies] = useState<ClassroomOccupancy[]>([]);
  const [loading, setLoading] = useState(false);

  // 加载教学楼
  useEffect(() => {
    loadBuildings();
  }, []);

  // 当选择教学楼或日期时，加载占用记录
  useEffect(() => {
    if (selectedBuilding && selectedDate) {
      loadOccupancies();
    }
  }, [selectedBuilding, selectedDate]);

  const loadBuildings = async () => {
    try {
      const response = await getBuildings();
      if (response.success) {
        setBuildings(response.data);
        if (response.data.length > 0) {
          setSelectedBuilding(response.data[0].id);
        }
      }
    } catch (error) {
      console.error('加载教学楼失败:', error);
    }
  };

  const loadOccupancies = async () => {
    if (!selectedBuilding || !selectedDate) return;

    setLoading(true);
    try {
      const response = await request.get<any, any>(
        `/occupancies/building/${selectedBuilding}?date=${selectedDate.format('YYYY-MM-DD')}`
      );
      if (response && response.data) {
        setOccupancies(response.data);
      }
    } catch (error) {
      console.error('加载占用记录失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const getTypeTag = (type: string) => {
    const typeMap: Record<string, { color: string; text: string }> = {
      '课程': { color: 'blue', text: '课程' },
      '会议': { color: 'purple', text: '会议' },
      '活动': { color: 'orange', text: '活动' },
      '维护': { color: 'red', text: '维护' },
    };
    const config = typeMap[type] || { color: 'default', text: type };
    return <Tag color={config.color}>{config.text}</Tag>;
  };

  const getStatusTag = (status: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      '已安排': { color: 'processing', text: '已安排' },
      '进行中': { color: 'success', text: '进行中' },
      '已完成': { color: 'default', text: '已完成' },
      '已取消': { color: 'error', text: '已取消' },
    };
    const config = statusMap[status] || { color: 'default', text: status };
    return <Tag color={config.color}>{config.text}</Tag>;
  };

  const columns = [
    {
      title: '教室',
      dataIndex: 'classroomName',
      key: 'classroomName',
      render: (text: string) => (
        <div>
          <EnvironmentOutlined style={{ marginRight: 8, color: '#1890ff' }} />
          {text}
        </div>
      ),
    },
    {
      title: '时间',
      key: 'time',
      render: (_: any, record: ClassroomOccupancy) => (
        <div>
          <ClockCircleOutlined style={{ marginRight: 8, color: '#52c41a' }} />
          {record.startTime} - {record.endTime}
        </div>
      ),
    },
    {
      title: '占用原因',
      dataIndex: 'reason',
      key: 'reason',
    },
    {
      title: '类型',
      dataIndex: 'type',
      key: 'type',
      render: (type: string) => getTypeTag(type),
    },
    {
      title: '负责人',
      dataIndex: 'occupiedBy',
      key: 'occupiedBy',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => getStatusTag(status),
    },
  ];

  return (
    <div className="classroom-status-container">
      <Card title="🏫 教室占用情况" className="status-card">
        <Alert
          message="教室占用说明"
          description="此页面显示教学楼内各教室的占用情况，包括课程、会议、活动等。占用期间的教室不可预约座位。"
          type="info"
          showIcon
          style={{ marginBottom: 24 }}
          closable
        />

        <div className="filter-section">
          <div className="filter-item">
            <label>选择教学楼：</label>
            <Select
              style={{ width: 200 }}
              value={selectedBuilding}
              onChange={setSelectedBuilding}
              options={buildings.map(b => ({
                label: b.name,
                value: b.id,
              }))}
            />
          </div>

          <div className="filter-item">
            <label>选择日期：</label>
            <DatePicker
              value={selectedDate}
              onChange={(date) => date && setSelectedDate(date)}
              format="YYYY-MM-DD"
              allowClear={false}
            />
          </div>
        </div>

        <Spin spinning={loading}>
          {occupancies.length === 0 ? (
            <Empty
              description="当天该教学楼暂无教室占用记录"
              style={{ marginTop: 40 }}
            />
          ) : (
            <div>
              <div style={{ marginBottom: 16, color: '#666' }}>
                共找到 <span style={{ color: '#1890ff', fontWeight: 'bold' }}>{occupancies.length}</span> 条占用记录
              </div>
              <Table
                columns={columns}
                dataSource={occupancies}
                rowKey="id"
                pagination={{
                  pageSize: 10,
                  showSizeChanger: true,
                  showTotal: (total) => `共 ${total} 条记录`,
                }}
              />
            </div>
          )}
        </Spin>
      </Card>
    </div>
  );
}
