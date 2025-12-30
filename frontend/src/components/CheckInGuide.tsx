import { Alert } from 'antd';

export default function CheckInGuide() {
  return (
    <Alert
      message="📋 签到说明"
      description={
        <div style={{ lineHeight: '1.8' }}>
          <p>为了确保座位的有效利用，请注意以下签到规则：</p>
          <ul style={{ marginBottom: 0, paddingLeft: 20 }}>
            <li><strong>签到时间：</strong>预约开始前30分钟至预约开始后15分钟</li>
            <li><strong>超时处理：</strong>超过预约开始15分钟未签到，系统将自动取消预约并标记为违约</li>
            <li><strong>签到方式：</strong>在"我的预约"页面点击"签到"按钮</li>
            <li><strong>签退提醒：</strong>使用完毕后请及时签退，以便他人使用</li>
          </ul>
        </div>
      }
      type="info"
      showIcon
      style={{ marginBottom: 16 }}
      closable
    />
  );
}
