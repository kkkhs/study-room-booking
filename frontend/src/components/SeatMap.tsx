import { useState } from 'react';
import { Card, Modal, message, Tag } from 'antd';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
import type { Seat } from '../types';
import './SeatMap.css';

interface SeatMapProps {
  seats: Seat[];
  onSelect: (seat: Seat) => void;
}

export default function SeatMap({ seats, onSelect }: SeatMapProps) {
  const [selectedSeat, setSelectedSeat] = useState<Seat | null>(null);
  const [modalVisible, setModalVisible] = useState(false);

  // 按行列组织座位数据
  const seatMap: Record<string, Seat | null> = {};
  seats.forEach(seat => {
    const key = `${seat.rowNum}-${seat.colNum}`;
    seatMap[key] = seat;
  });

  const handleSeatClick = (seat: Seat | null) => {
    if (!seat) {
      message.warning('该座位不存在');
      return;
    }
    
    if (seat.status !== 'AVAILABLE') {
      message.warning('该座位已被预约');
      return;
    }

    setSelectedSeat(seat);
    setModalVisible(true);
  };

  const handleConfirm = () => {
    if (selectedSeat) {
      onSelect(selectedSeat);
      setModalVisible(false);
      setSelectedSeat(null);
    }
  };

  const modalContent = selectedSeat ? (
    <div>
      <p><strong>座位信息：</strong>{selectedSeat.seatNumber}</p>
      <p><strong>位置：</strong>第 {selectedSeat.rowNum} 排 第 {selectedSeat.colNum} 列</p>
      <div style={{ 
        marginTop: 16, 
        padding: 12, 
        background: '#e6f7ff', 
        border: '1px solid #91d5ff',
        borderRadius: 4 
      }}>
        <p style={{ margin: 0, fontWeight: 'bold', color: '#1890ff' }}>📋 签到提醒</p>
        <p style={{ margin: '8px 0 0 0', fontSize: '13px', lineHeight: '1.6' }}>
          预约成功后，请在预约开始前30分钟至预约开始后15分钟内完成签到，
          超时未签到将自动取消预约并标记为违约。
        </p>
      </div>
    </div>
  ) : null;

  // 渲染座位网格（10行10列）
  const renderSeatGrid = () => {
    const rows = [];
    for (let row = 1; row <= 10; row++) {
      const cols = [];
      for (let col = 1; col <= 10; col++) {
        const key = `${row}-${col}`;
        const seat = seatMap[key];
        
        cols.push(
          <div
            key={key}
            className={`seat ${seat ? (seat.status === 'AVAILABLE' ? 'available' : 'occupied') : 'empty'}`}
            onClick={() => handleSeatClick(seat)}
            title={seat ? `座位 ${seat.seatNumber}` : '无座位'}
          >
            <div className="seat-number">{col}</div>
          </div>
        );
      }
      rows.push(
        <div key={row} className="seat-row">
          <div className="row-label">{row}排</div>
          <div className="seat-cols">{cols}</div>
        </div>
      );
    }
    return rows;
  };

  const availableCount = seats.filter(s => s.status === 'AVAILABLE').length;
  const occupiedCount = seats.length - availableCount;

  return (
    <div className="seat-map-container">
      <Card 
        className="seat-map-card"
        title={
          <div className="seat-map-header">
            <span>🎬 座位布局</span>
            <div className="seat-legend">
              <Tag icon={<CheckCircleOutlined />} color="success">
                可选 ({availableCount})
              </Tag>
              <Tag icon={<CloseCircleOutlined />} color="default">
                已占 ({occupiedCount})
              </Tag>
            </div>
          </div>
        }
      >
        <div className="screen-area">
          <div className="screen">讲 台</div>
        </div>
        
        <div className="seat-grid">
          {renderSeatGrid()}
        </div>

        <div className="seat-tips">
          💡 提示：点击绿色座位即可预约
        </div>
      </Card>

      <Modal
        title="确认预约"
        open={modalVisible}
        onOk={handleConfirm}
        onCancel={() => setModalVisible(false)}
        okText="确认预约"
        cancelText="取消"
        width={500}
      >
        {modalContent}
      </Modal>
    </div>
  );
}
