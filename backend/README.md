# 🔧 后端服务 - 自习室预约系统

Spring Boot 3.2.1 + Java 17 后端服务

## 📋 目录

- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API接口文档](#api-接口文档)
- [数据库设计](#数据库设计)
- [核心功能](#核心功能)
- [开发指南](#开发指南)

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | JDK版本 |
| Spring Boot | 3.2.1 | 基础框架 |
| Spring Data JPA | 3.2.1 | ORM框架 |
| H2 Database | 2.2.224 | 嵌入式数据库 |
| Lombok | 1.18.30 | 简化代码 |
| Maven | 3.9+ | 构建工具 |

## 📁 项目结构

```
backend/
├── src/main/
│   ├── java/com/hfut/studyroom/
│   │   ├── StudyRoomApplication.java    # 启动类
│   │   ├── entity/                      # 实体类
│   │   │   ├── User.java                # 用户实体
│   │   │   ├── Building.java            # 教学楼实体
│   │   │   ├── Classroom.java           # 教室实体
│   │   │   ├── Seat.java                # 座位实体
│   │   │   ├── Booking.java             # 预约实体
│   │   │   ├── Blacklist.java           # 黑名单实体
│   │   │   └── ClassroomOccupancy.java  # 教室占用实体
│   │   ├── repository/                  # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   ├── BuildingRepository.java
│   │   │   ├── ClassroomRepository.java
│   │   │   ├── SeatRepository.java
│   │   │   ├── BookingRepository.java
│   │   │   ├── BlacklistRepository.java
│   │   │   └── ClassroomOccupancyRepository.java
│   │   ├── service/                     # 业务逻辑层
│   │   │   ├── AuthService.java         # 认证服务
│   │   │   ├── BookingService.java      # 预约服务
│   │   │   ├── SeatService.java         # 座位服务
│   │   │   ├── AdminService.java        # 管理服务
│   │   │   └── ClassroomOccupancyService.java  # 教室占用服务
│   │   ├── controller/                  # 控制器层
│   │   │   ├── AuthController.java      # 认证接口
│   │   │   ├── BuildingController.java  # 教学楼接口
│   │   │   ├── ClassroomController.java # 教室接口
│   │   │   ├── SeatController.java      # 座位接口
│   │   │   ├── BookingController.java   # 预约接口
│   │   │   ├── AdminController.java     # 管理接口
│   │   │   └── ClassroomOccupancyController.java  # 教室占用接口
│   │   ├── dto/                         # 数据传输对象
│   │   │   ├── ApiResponse.java         # 统一响应格式
│   │   │   ├── LoginRequest.java        # 登录请求
│   │   │   ├── LoginResponse.java       # 登录响应
│   │   │   ├── RegisterRequest.java     # 注册请求
│   │   │   ├── BookingRequest.java      # 预约请求
│   │   │   ├── BookingDTO.java          # 预约DTO
│   │   │   ├── SeatDTO.java             # 座位DTO
│   │   │   ├── UserDTO.java             # 用户DTO
│   │   │   ├── BlacklistDTO.java        # 黑名单DTO
│   │   │   ├── ClassroomDTO.java        # 教室DTO
│   │   │   ├── ClassroomOccupancyDTO.java  # 教室占用DTO
│   │   │   └── ClassroomOccupancyRequest.java  # 教室占用请求
│   │   ├── exception/                   # 异常处理
│   │   │   ├── BusinessException.java   # 业务异常
│   │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   ├── config/                      # 配置类
│   │   │   ├── CorsConfig.java          # 跨域配置
│   │   │   └── SecurityConfig.java      # 安全配置
│   │   ├── scheduler/                   # 定时任务
│   │   │   └── BookingScheduler.java    # 预约定时任务
│   │   └── init/                        # 数据初始化
│   │       └── DataInitializer.java     # 示例数据初始化
│   └── resources/
│       └── application.yml              # 应用配置
├── build.sh                             # 构建脚本
├── run.sh                               # 运行脚本
├── pom.xml                              # Maven配置
└── data/                                # H2数据库文件（运行时生成）
    └── studyroom.mv.db
```

## 🚀 快速开始

### 环境要求

- Java 17+
- Maven 3.9+

### 构建项目

```bash
cd backend
./build.sh
```

构建脚本会执行：
1. 清理旧的构建文件
2. 编译项目
3. 运行测试（可跳过）
4. 打包为可执行JAR

### 启动服务

```bash
./run.sh
```

启动脚本会：
1. 检查 Java 版本
2. 启动 Spring Boot 应用
3. 初始化示例数据
4. 日志输出到 `backend.log`

服务启动后访问：http://localhost:8080

### 手动启动（不使用脚本）

```bash
# 构建
mvn clean package -DskipTests

# 运行
java -jar target/study-room-booking-1.0.0.jar
```

## ⚙️ 配置说明

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  application:
    name: study-room-booking
    
  datasource:
    url: jdbc:h2:file:./data/studyroom
    driver-class-name: org.h2.Driver
    username: sa
    password:
    
  jpa:
    hibernate:
      ddl-auto: update  # 自动更新表结构
    show-sql: false      # 是否显示SQL
    properties:
      hibernate:
        format_sql: true
        
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true

app:
  booking:
    timeout-minutes: 15  # 签到超时时间（分钟）
```

### 修改端口

编辑 `application.yml`：

```yaml
server:
  port: 9090  # 改为其他端口
```

### 切换数据库

生产环境建议使用 MySQL/PostgreSQL：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/studyroom
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

## 📡 API 接口文档

### 通用响应格式

```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "data": {}
}
```

### 认证接口

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "student1",
  "password": "password123",
  "name": "张三",
  "studentId": "2021001",
  "phone": "13800138000",
  "email": "student1@hfut.edu.cn"
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "student1",
  "password": "password123"
}
```

**响应：**
```json
{
  "code": 200,
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "uuid-token-string",
    "user": {
      "id": 1,
      "username": "student1",
      "name": "张三",
      "role": "USER"
    }
  }
}
```

### 教学楼接口

#### 获取所有教学楼
```http
GET /api/buildings
```

### 教室接口

#### 获取教室列表
```http
GET /api/classrooms?buildingId=1
```

#### 获取教室座位
```http
GET /api/classrooms/1/seats?date=2025-12-31&startTime=08:00:00&endTime=12:00:00
```

### 预约接口

#### 创建预约
```http
POST /api/bookings
Content-Type: application/json
Authorization: Bearer {token}

{
  "seatId": 1,
  "startTime": "2025-12-31 08:00:00",
  "endTime": "2025-12-31 12:00:00"
}
```

#### 获取我的预约
```http
GET /api/bookings/my
Authorization: Bearer {token}
```

#### 签到
```http
POST /api/bookings/1/checkin
Authorization: Bearer {token}
```

#### 取消预约
```http
DELETE /api/bookings/1
Authorization: Bearer {token}
```

### 教室占用接口

#### 获取所有占用记录
```http
GET /api/classroom-occupancy
```

#### 创建占用记录（管理员）
```http
POST /api/classroom-occupancy
Content-Type: application/json
Authorization: Bearer {admin-token}

{
  "classroomId": 1,
  "occupancyDate": "2025-12-31",
  "startTime": "14:00",
  "endTime": "16:00",
  "type": "COURSE",
  "reason": "数据结构课程",
  "occupiedBy": "张教授"
}
```

### 管理员接口

#### 获取所有用户
```http
GET /api/admin/users
Authorization: Bearer {admin-token}
```

#### 添加黑名单
```http
POST /api/admin/blacklist?userId=3&reason=违约3次&adminId=1
Authorization: Bearer {admin-token}
```

#### 获取统计数据
```http
GET /api/admin/statistics
Authorization: Bearer {admin-token}
```

## 🗄️ 数据库设计

### 表结构

#### app_user（用户表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(255) | 密码（加密） |
| real_name | VARCHAR(100) | 真实姓名 |
| student_id | VARCHAR(20) | 学号 |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(100) | 邮箱 |
| role | VARCHAR(20) | 角色（USER/ADMIN） |
| status | VARCHAR(20) | 状态（ACTIVE/DISABLED） |
| violation_count | INT | 违约次数 |
| created_at | TIMESTAMP | 创建时间 |
| last_login_time | TIMESTAMP | 最后登录时间 |

#### building（教学楼表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 教学楼名称 |
| location | VARCHAR(200) | 位置 |
| floors | INT | 楼层数 |
| status | VARCHAR(20) | 状态（OPEN/CLOSED） |
| open_time | TIME | 开放时间 |
| close_time | TIME | 关闭时间 |

#### classroom（教室表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| building_id | BIGINT | 所属教学楼 |
| room_number | VARCHAR(20) | 教室编号 |
| floor | INT | 楼层 |
| capacity | INT | 容量 |
| status | VARCHAR(20) | 状态 |

#### seat（座位表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| classroom_id | BIGINT | 所属教室 |
| seat_number | VARCHAR(20) | 座位号 |
| row_num | INT | 行号 |
| col_num | INT | 列号 |
| location | VARCHAR(100) | 位置描述 |
| status | VARCHAR(20) | 状态 |

#### booking（预约表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| seat_id | BIGINT | 座位ID |
| booking_date | DATE | 预约日期 |
| start_time | TIME | 开始时间 |
| end_time | TIME | 结束时间 |
| status | VARCHAR(20) | 状态（PENDING/ACTIVE/COMPLETED/CANCELLED/VIOLATED/TIMEOUT） |
| check_in_time | TIMESTAMP | 签到时间 |
| check_out_time | TIMESTAMP | 签退时间 |
| created_at | TIMESTAMP | 创建时间 |

#### blacklist（黑名单表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID（唯一） |
| reason | VARCHAR(200) | 拉黑原因 |
| created_by | BIGINT | 操作管理员ID |
| created_at | TIMESTAMP | 创建时间 |

#### classroom_occupancy（教室占用表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| classroom_id | BIGINT | 教室ID |
| occupancy_date | DATE | 占用日期 |
| start_time | TIME | 开始时间 |
| end_time | TIME | 结束时间 |
| type | VARCHAR(20) | 类型（COURSE/MEETING/MAINTENANCE/OTHER） |
| reason | VARCHAR(200) | 占用原因 |
| occupied_by | VARCHAR(100) | 占用人 |
| status | VARCHAR(20) | 状态（ACTIVE/CANCELLED） |
| created_at | TIMESTAMP | 创建时间 |

## 🔑 核心功能

### 1. 定时任务

#### 超时预约处理
- **频率**：每分钟执行一次
- **功能**：检测超过签到窗口的 PENDING 预约，自动设为 TIMEOUT
- **代码**：`scheduler/BookingScheduler.java`

```java
@Scheduled(cron = "0 * * * * ?")
public void releaseTimeoutBookings() {
    // 查找所有 PENDING 状态的预约
    // 计算是否超过 开始时间 + 15分钟
    // 设置为 TIMEOUT 状态
}
```

#### 预约自动完成
- **频率**：每小时执行一次
- **功能**：将已过结束时间的 ACTIVE 预约自动设为 COMPLETED

### 2. 业务规则

#### 预约限制
- 每个用户每天只能预约一次
- 同一座位同一时间段只能被一个用户预约
- 黑名单用户无法预约

#### 签到规则
- 签到窗口：开始前30分钟 ～ 开始后15分钟
- 超时未签到：自动标记为 TIMEOUT（违约）
- 已签到：状态变为 ACTIVE

#### 黑名单规则
- 管理员可拉黑用户
- 拉黑后用户无法预约（可以登录查看）
- 管理员可解除拉黑

### 3. 数据初始化

系统首次启动会自动初始化：
- 2栋教学楼（新安学堂、博学楼）
- 300个教室（每栋楼5层，每层30个教室）
- 30000个座位（每教室100个座位，10×10布局）
- 3个测试用户（admin, user1, user2）
- 示例教室占用记录

**代码**：`init/DataInitializer.java`

## 🛠️ 开发指南

### 添加新实体

1. 在 `entity/` 创建实体类
2. 在 `repository/` 创建 Repository 接口
3. 在 `service/` 实现业务逻辑
4. 在 `controller/` 创建 REST API
5. 在 `dto/` 创建 DTO（如需要）

### 异常处理

使用 `BusinessException` 抛出业务异常：

```java
if (user == null) {
    throw new BusinessException("用户不存在");
}
```

全局异常处理器会自动捕获并返回统一格式。

### 日志记录

使用 Lombok 的 `@Slf4j` 注解：

```java
@Slf4j
@Service
public class MyService {
    public void doSomething() {
        log.info("执行操作");
        log.error("发生错误", exception);
    }
}
```

### 测试

```bash
# 运行所有测试
mvn test

# 运行指定测试
mvn test -Dtest=UserServiceTest
```

## 🐛 常见问题

### Q1：H2数据库连接失败
**A**：检查 `data/` 目录是否有写权限，或删除后重新启动。

### Q2：端口被占用
**A**：修改 `application.yml` 中的 `server.port`。

### Q3：如何重置数据库
**A**：删除 `backend/data/` 目录，重启服务会自动初始化。

### Q4：定时任务不执行
**A**：确保主类上有 `@EnableScheduling` 注解。

## 📝 待办事项

- [ ] 集成 Swagger API 文档
- [ ] 添加单元测试覆盖
- [ ] 支持 Redis 缓存
- [ ] 支持 MySQL 数据库
- [ ] 添加日志AOP切面
- [ ] 集成 Spring Security JWT

---

**技术支持**：HFUT Study Room Booking Team
