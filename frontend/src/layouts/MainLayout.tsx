import { Layout, Menu, Avatar, Dropdown, message } from 'antd';
import {
  HomeOutlined,
  BookOutlined,
  UserOutlined,
  LogoutOutlined,
  DashboardOutlined,
  EnvironmentOutlined,
} from '@ant-design/icons';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import { getUser, clearAuth, isAdmin } from '../utils/auth';
import type { MenuProps } from 'antd';

const { Header, Content, Footer } = Layout;

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const user = getUser();

  // 处理退出登录
  const handleLogout = () => {
    clearAuth();
    message.success('退出登录成功');
    navigate('/login');
  };

  // 用户下拉菜单
  const userMenuItems: MenuProps['items'] = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人信息',
      onClick: () => navigate('/profile'),
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  // 导航菜单项
  const menuItems: MenuProps['items'] = [
    {
      key: '/home',
      icon: <HomeOutlined />,
      label: '首页',
    },
    {
      key: '/booking',
      icon: <BookOutlined />,
      label: '座位预约',
    },
    {
      key: '/classroom-status',
      icon: <EnvironmentOutlined />,
      label: '教室状态',
    },
  ];

  // 如果是管理员，添加管理菜单
  if (isAdmin()) {
    menuItems.push({
      key: '/admin',
      icon: <DashboardOutlined />,
      label: '系统管理',
    });
  }

  // 处理菜单点击
  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key);
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'space-between',
        backgroundColor: '#001529',
        padding: '0 24px',
      }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <div style={{ 
            color: 'white', 
            fontSize: '20px', 
            fontWeight: 'bold',
            marginRight: '48px',
          }}>
            📚 自习室预约系统
          </div>
          <Menu
            theme="dark"
            mode="horizontal"
            selectedKeys={[location.pathname]}
            items={menuItems}
            onClick={handleMenuClick}
            style={{ flex: 1, minWidth: 0, border: 'none' }}
          />
        </div>
        
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center' }}>
            <Avatar icon={<UserOutlined />} style={{ marginRight: '8px' }} />
            <span style={{ color: 'white' }}>{user?.name || user?.username}</span>
          </div>
        </Dropdown>
      </Header>

      <Content style={{ padding: '24px', backgroundColor: '#f0f2f5' }}>
        <div style={{ 
          maxWidth: '1400px', 
          margin: '0 auto',
          minHeight: 'calc(100vh - 134px)',
        }}>
          <Outlet />
        </div>
      </Content>

      <Footer style={{ textAlign: 'center', backgroundColor: '#f0f2f5' }}>
        自习室预约系统 ©{new Date().getFullYear()} 合肥工业大学宣城校区
      </Footer>
    </Layout>
  );
}
