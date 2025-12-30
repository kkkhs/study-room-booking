# 🎨 前端应用 - 自习室预约系统

React 19 + TypeScript 5 + Ant Design 6 前端应用

## 📋 目录

- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [页面说明](#页面说明)
- [核心组件](#核心组件)
- [API服务](#api-服务)
- [开发指南](#开发指南)

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| React | 19 | UI 框架 |
| TypeScript | 5 | 编程语言 |
| Vite | 7 | 构建工具 |
| Ant Design | 6 | UI 组件库 |
| React Router | 7 | 路由管理 |
| Axios | 1.6+ | HTTP 客户端 |
| Day.js | 1.11+ | 日期处理 |

## 📁 项目结构

```
frontend/
├── public/
│   └── vite.svg                 # 网站图标
├── src/
│   ├── App.tsx                  # 应用根组件
│   ├── main.tsx                 # 应用入口
│   ├── index.css                # 全局样式
│   ├── layouts/                 # 布局组件
│   │   └── MainLayout.tsx       # 主布局（导航栏+侧边栏）
│   ├── pages/                   # 页面组件
│   │   ├── Login/               # 登录/注册页
│   │   │   ├── index.tsx
│   │   │   └── style.css
│   │   ├── Home/                # 用户首页
│   │   │   ├── index.tsx
│   │   │   └── style.css
│   │   ├── Booking/             # 座位预约页
│   │   │   ├── index.tsx
│   │   │   └── style.css
│   │   ├── ClassroomStatus/     # 教室状态页
│   │   │   ├── index.tsx
│   │   │   └── style.css
│   │   └── Admin/               # 管理后台页
│   │       ├── index.tsx
│   │       └── style.css
│   ├── components/              # 通用组件
│   │   ├── SeatMap.tsx          # 座位地图组件
│   │   ├── SeatMap.css
│   │   └── CheckInGuide.tsx     # 签到指南组件
│   ├── services/                # API 服务
│   │   └── api.ts               # API 接口定义
│   ├── types/                   # TypeScript 类型
│   │   └── index.ts             # 类型定义
│   ├── utils/                   # 工具函数
│   │   ├── auth.ts              # 认证工具
│   │   └── request.ts           # Axios 封装
│   ├── vite-env.d.ts            # Vite 类型声明
│   └── ...
├── .gitignore
├── package.json                 # npm 依赖配置
├── tsconfig.json                # TypeScript 配置
├── vite.config.ts               # Vite 配置
├── start.sh                     # 启动脚本
└── README.md                    # 本文档
```

## 🚀 快速开始

### 环境要求

- Node.js 20.19+ 或 22.12+
- npm 9+ 或 yarn 1.22+

### 安装依赖

```bash
cd frontend
npm install
```

### 启动开发服务器

#### 使用启动脚本（推荐）

```bash
./start.sh
```

脚本会自动：
1. 检查并安装依赖
2. 启动开发服务器
3. 自动打开浏览器

#### 手动启动

```bash
npm run dev
```

访问：http://localhost:5173

### 构建生产版本

```bash
npm run build
```

构建输出到 `dist/` 目录。

### 预览生产构建

```bash
npm run preview
```

## 📄 页面说明

### 1. 登录/注册页 (`/login`)

**路径**：`src/pages/Login/`

**功能**：
- 用户登录
- 新用户注册
- 表单验证
- 自动跳转

**路由**：`/login`

### 2. 用户首页 (`/`)

**路径**：`src/pages/Home/`

**功能**：
- 显示个人预约统计（总预约、今日预约、进行中预约）
- 显示预约列表（支持签到、取消、查看详情）
- 签到状态提示（倒计时、超时提醒）
- 预约状态标签（待签到、使用中、已完成等）

**路由**：`/`

### 3. 座位预约页 (`/booking`)

**路径**：`src/pages/Booking/`

**功能**：
- 选择教学楼和教室
- 选择日期和时间（智能时间选择）
  - 默认：下一个整点小时
  - 步长：30分钟
  - 禁用过去时间
- 可视化座位地图（10×10）
- 实时座位状态（可用/已占用）
- 一键预约

**路由**：`/booking`

**流程**：
```
1. 选择教学楼 → 2. 选择教室 → 3. 设置时间 → 4. 查看座位 → 5. 选择座位 → 6. 确认预约
```

### 4. 教室状态页 (`/classroom-status`)

**路径**：`src/pages/ClassroomStatus/`

**功能**：
- 查看各教学楼教室占用情况
- 按教学楼筛选
- 按日期筛选
- 显示占用类型（课程、会议、维护等）
- 显示占用时间和负责人

**路由**：`/classroom-status`

### 5. 管理后台页 (`/admin`)

**路径**：`src/pages/Admin/`

**功能**：
- **用户管理**：查看所有用户、黑名单状态
- **预约管理**：查看所有预约记录
- **黑名单管理**：拉黑/解除拉黑用户
- **统计数据**：系统整体数据统计

**路由**：`/admin`（需管理员权限）

## 🧩 核心组件

### SeatMap（座位地图）

**路径**：`src/components/SeatMap.tsx`

**功能**：
- 可视化展示 10×10 座位布局
- 动态显示座位状态
  - 🟢 绿色：可用
  - ⚪ 灰色：已占用
- 点击座位触发预约
- 预约确认对话框

**使用示例**：
```tsx
<SeatMap
  seats={seats}
  onSelectSeat={handleSelectSeat}
  loading={loading}
/>
```

### CheckInGuide（签到指南）

**路径**：`src/components/CheckInGuide.tsx`

**功能**：
- 显示签到规则说明
- 签到窗口时间提示
- 超时后果警告

**使用示例**：
```tsx
{hasPendingBookings && <CheckInGuide />}
```

### MainLayout（主布局）

**路径**：`src/layouts/MainLayout.tsx`

**功能**：
- 顶部导航栏（用户信息、退出登录）
- 侧边栏菜单（首页、预约、教室状态、管理后台）
- 响应式布局
- 权限控制（管理员菜单）

## 🔌 API 服务

### 认证 API

**文件**：`src/services/api.ts`

```typescript
// 登录
login(data: { username: string; password: string })

// 注册
register(data: RegisterRequest)
```

### 预约 API

```typescript
// 获取我的预约
getMyBookings()

// 创建预约
createBooking(data: BookingRequest)

// 签到
checkIn(bookingId: number)

// 取消预约
cancelBooking(bookingId: number)
```

### 教学楼和教室 API

```typescript
// 获取所有教学楼
getBuildings()

// 获取教室列表
getClassroomsByBuilding(buildingId: number)

// 获取教室详情
getClassroomById(classroomId: number)
```

### 管理员 API

```typescript
// 获取所有用户
getAllUsers()

// 获取所有预约
getAllBookings()

// 获取黑名单
getBlacklist()

// 添加黑名单
addBlacklist(userId: number, reason: string, adminId: number)

// 移除黑名单
removeBlacklist(blacklistId: number)

// 获取统计数据
getStatistics()
```

## 🎨 样式说明

### 主题色

```css
:root {
  --primary-color: #1890ff;     /* 主色调（蓝色） */
  --success-color: #52c41a;     /* 成功色（绿色） */
  --warning-color: #faad14;     /* 警告色（黄色） */
  --error-color: #ff4d4f;       /* 错误色（红色） */
  --text-color: #000000d9;      /* 文本色 */
  --background-color: #f0f2f5;  /* 背景色 */
}
```

### 响应式断点

```css
/* 移动端 */
@media (max-width: 768px) { }

/* 平板 */
@media (min-width: 768px) and (max-width: 1024px) { }

/* 桌面端 */
@media (min-width: 1024px) { }
```

## 🛠️ 开发指南

### 添加新页面

1. 在 `src/pages/` 创建页面目录
2. 创建 `index.tsx` 和 `style.css`
3. 在 `src/App.tsx` 添加路由
4. 在 `src/layouts/MainLayout.tsx` 添加菜单（如需要）

示例：
```tsx
// src/pages/NewPage/index.tsx
export default function NewPage() {
  return <div>New Page</div>;
}

// src/App.tsx
import NewPage from './pages/NewPage';

<Route path="/new-page" element={<NewPage />} />

// src/layouts/MainLayout.tsx
{
  key: 'new-page',
  icon: <IconName />,
  label: <Link to="/new-page">新页面</Link>,
}
```

### 添加新 API

在 `src/services/api.ts` 添加：

```typescript
export const myNewApi = (params: any) => {
  return request.get<ApiResponse<any>>('/my-api', { params });
};
```

### 添加新类型

在 `src/types/index.ts` 添加：

```typescript
export interface MyType {
  id: number;
  name: string;
  // ...
}
```

### 状态管理

目前使用 React 内置的 `useState` 和 `useEffect`。

如需全局状态管理，可考虑：
- Context API
- Zustand
- Redux Toolkit

### 表单处理

使用 Ant Design 的 `Form` 组件：

```tsx
const [form] = Form.useForm();

<Form form={form} onFinish={handleSubmit}>
  <Form.Item name="username" rules={[{ required: true }]}>
    <Input />
  </Form.Item>
  <Button type="primary" htmlType="submit">
    提交
  </Button>
</Form>
```

### 消息提示

```tsx
import { message } from 'antd';

message.success('操作成功');
message.error('操作失败');
message.warning('警告信息');
message.info('提示信息');
```

### 路由跳转

```tsx
import { useNavigate } from 'react-router-dom';

const navigate = useNavigate();

// 跳转
navigate('/booking');

// 带参数跳转
navigate('/booking', { state: { id: 1 } });

// 返回上一页
navigate(-1);
```

## ⚙️ 配置说明

### Vite 配置

**文件**：`vite.config.ts`

```typescript
export default defineConfig({
  server: {
    port: 5173,           // 开发服务器端口
    proxy: {              // API 代理（可选）
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'dist',       // 构建输出目录
    sourcemap: false,     // 是否生成 sourcemap
  },
});
```

### TypeScript 配置

**文件**：`tsconfig.json`

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "jsx": "react-jsx",
    "strict": true,
    "moduleResolution": "bundler"
  }
}
```

### 修改后端 API 地址

**文件**：`src/utils/request.ts`

```typescript
const request = axios.create({
  baseURL: 'http://localhost:8080/api',  // 修改为实际后端地址
  timeout: 10000,
});
```

## 🧪 测试

### 运行测试

```bash
npm run test
```

### 类型检查

```bash
npm run type-check
```

### ESLint 检查

```bash
npm run lint
```

## 📦 构建部署

### 构建生产版本

```bash
npm run build
```

### 部署到 Nginx

```bash
# 1. 构建
npm run build

# 2. 复制 dist/ 到 Nginx 目录
cp -r dist/* /var/www/html/

# 3. 配置 Nginx
server {
  listen 80;
  server_name your-domain.com;
  root /var/www/html;
  
  location / {
    try_files $uri $uri/ /index.html;
  }
  
  location /api {
    proxy_pass http://localhost:8080/api;
  }
}

# 4. 重启 Nginx
sudo systemctl restart nginx
```

### 部署到 Vercel

```bash
# 安装 Vercel CLI
npm i -g vercel

# 登录
vercel login

# 部署
vercel
```

## 🐛 常见问题

### Q1：npm install 失败
**A**：尝试清除缓存后重新安装：
```bash
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Q2：开发服务器无法访问
**A**：检查端口是否被占用，或修改 `vite.config.ts` 中的端口。

### Q3：API 请求失败（CORS）
**A**：确保后端已配置 CORS，或在 `vite.config.ts` 中配置代理。

### Q4：构建后路由 404
**A**：确保服务器配置了 SPA 重定向规则（所有路由指向 index.html）。

### Q5：类型错误
**A**：检查 `src/types/index.ts` 中的类型定义是否与后端一致。

## 📝 待办事项

- [ ] 添加单元测试（Jest + React Testing Library）
- [ ] 添加 E2E 测试（Playwright）
- [ ] 集成全局状态管理（Zustand）
- [ ] 优化 Bundle 大小（代码分割）
- [ ] 添加 PWA 支持
- [ ] 国际化（i18n）
- [ ] 主题切换（暗黑模式）
- [ ] 移动端适配优化

## 🎯 最佳实践

### 组件拆分
- 保持组件单一职责
- 提取可复用组件到 `components/`
- 页面级组件放在 `pages/`

### 样式管理
- 使用 CSS Modules 或 CSS-in-JS
- 避免全局样式污染
- 统一使用 Ant Design 主题变量

### 性能优化
- 使用 `React.memo` 避免不必要的重渲染
- 使用 `useMemo` 和 `useCallback` 优化计算和回调
- 路由懒加载：`React.lazy(() => import('./Page'))`

### 代码规范
- 使用 ESLint 和 Prettier
- 遵循 Airbnb JavaScript Style Guide
- 提交前运行 `npm run lint`

---

**技术支持**：HFUT Study Room Booking Team
