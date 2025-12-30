# 🚀 部署指南

本文档提供项目的免费部署方案和详细步骤。

## 📋 目录

- [部署架构](#部署架构)
- [前端部署](#前端部署)
- [后端部署](#后端部署)
- [完整部署流程](#完整部署流程)
- [环境变量配置](#环境变量配置)
- [常见问题](#常见问题)

## 🏗️ 部署架构

由于前后端分离，需要分别部署：

```
┌─────────────────────────────────────────────────────────────┐
│                      用户浏览器                              │
└─────────────────────────────────────────────────────────────┘
                           │
          ┌────────────────┴───────────────────┐
          ↓                                    ↓
┌─────────────────────┐            ┌─────────────────────┐
│   前端静态资源       │            │    后端 API 服务     │
│   (Vercel/Netlify)  │────HTTP───→│  (Railway/Render)   │
│   React SPA         │            │   Spring Boot       │
└─────────────────────┘            └─────────────────────┘
```

## 🎨 前端部署

### 方案 1：Vercel（推荐）⭐

#### 特点
- ✅ **完全免费**（个人项目）
- ✅ 自动 HTTPS
- ✅ 全球 CDN
- ✅ 自动部署（推送代码即部署）
- ✅ 预览环境（PR 自动预览）

#### 部署步骤

##### 1. 准备工作

```bash
# 1. 确保前端可以正常构建
cd frontend
npm install
npm run build  # 确保无错误

# 2. 确认 package.json 中的构建命令
# "scripts": {
#   "build": "tsc -b && vite build"
# }
```

##### 2. 创建配置文件

在 `frontend/` 目录创建 `vercel.json`：

```json
{
  "version": 2,
  "buildCommand": "npm run build",
  "outputDirectory": "dist",
  "rewrites": [
    {
      "source": "/(.*)",
      "destination": "/index.html"
    }
  ],
  "env": {
    "VITE_API_BASE_URL": "https://your-backend-api.railway.app/api"
  }
}
```

##### 3. 部署到 Vercel

**方式 A：通过 Vercel CLI**

```bash
# 安装 Vercel CLI
npm install -g vercel

# 登录
vercel login

# 部署（在 frontend 目录）
cd frontend
vercel

# 生产环境部署
vercel --prod
```

**方式 B：通过 Vercel 网站（推荐）**

1. 访问 [vercel.com](https://vercel.com)
2. 使用 GitHub 账号登录
3. 点击 "New Project"
4. 选择你的 GitHub 仓库
5. 配置项目：
   ```
   Framework Preset: Vite
   Root Directory: frontend
   Build Command: npm run build
   Output Directory: dist
   ```
6. 添加环境变量：
   ```
   VITE_API_BASE_URL = https://your-backend-api.railway.app/api
   ```
7. 点击 "Deploy"

##### 4. 修改前端配置

编辑 `frontend/src/utils/request.ts`：

```typescript
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const request = axios.create({
  baseURL,
  timeout: 10000,
});
```

##### 5. 自动部署设置

- Push 到 `main` 分支 → 自动部署到生产环境
- Push 到其他分支 → 自动创建预览环境
- 每个 PR → 自动创建预览 URL

### 方案 2：Netlify

#### 部署步骤

1. 访问 [netlify.com](https://netlify.com)
2. 连接 GitHub 仓库
3. 配置构建：
   ```
   Base directory: frontend
   Build command: npm run build
   Publish directory: frontend/dist
   ```
4. 添加环境变量：
   ```
   VITE_API_BASE_URL = https://your-backend-api.railway.app/api
   ```
5. 配置重定向（在 `frontend/public/_redirects`）：
   ```
   /*    /index.html   200
   ```

### 方案 3：GitHub Pages

**注意**：GitHub Pages 只支持静态网站，不支持服务端环境变量。

1. 修改 `vite.config.ts` 添加 `base`:
   ```typescript
   export default defineConfig({
     base: '/your-repo-name/',
     // ...
   })
   ```

2. 部署脚本（`frontend/deploy-gh-pages.sh`）：
   ```bash
   #!/bin/bash
   npm run build
   cd dist
   git init
   git add -A
   git commit -m 'Deploy'
   git push -f git@github.com:username/repo.git main:gh-pages
   ```

## 🔧 后端部署

### 方案 1：Railway（推荐）⭐

#### 特点
- ✅ 每月 $5 免费额度
- ✅ 支持 Spring Boot
- ✅ 自动 HTTPS
- ✅ 支持 PostgreSQL（免费）
- ✅ 自动部署

#### 部署步骤

##### 1. 准备 Dockerfile

在 `backend/` 创建 `Dockerfile`：

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

##### 2. 部署到 Railway

1. 访问 [railway.app](https://railway.app)
2. 使用 GitHub 登录
3. 点击 "New Project"
4. 选择 "Deploy from GitHub repo"
5. 选择你的仓库
6. Railway 会自动检测到 Dockerfile
7. 配置：
   ```
   Root Directory: backend
   Start Command: 自动检测
   ```

##### 3. 添加环境变量

在 Railway 项目设置中添加：

```bash
# 数据库（如果使用 Railway PostgreSQL）
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=...

# 或者继续使用 H2（开发测试）
SPRING_DATASOURCE_URL=jdbc:h2:mem:studyroom
```

##### 4. 获取部署 URL

部署完成后，Railway 会提供一个 URL，例如：
```
https://your-app-name.railway.app
```

这个 URL 就是你的后端 API 地址。

### 方案 2：Render

#### 部署步骤

1. 访问 [render.com](https://render.com)
2. 连接 GitHub
3. 创建 "New Web Service"
4. 配置：
   ```
   Build Command: cd backend && ./mvnw clean package -DskipTests
   Start Command: java -jar backend/target/study-room-booking-1.0.0.jar
   ```
5. 选择免费计划（Free tier）
6. 添加环境变量

**注意**：免费计划会在 15 分钟无活动后休眠。

### 方案 3：Fly.io

提供更多免费资源，但配置较复杂。

```bash
# 安装 flyctl
curl -L https://fly.io/install.sh | sh

# 登录
flyctl auth login

# 初始化（在 backend 目录）
cd backend
flyctl launch

# 部署
flyctl deploy
```

## 🔄 完整部署流程

### 步骤 1：上传到 GitHub

```bash
# 1. 初始化 Git 仓库（如果还没有）
cd /Users/bytedance/code/library
git init

# 2. 添加所有文件
git add .

# 3. 创建首次提交
git commit -m "Initial commit: Study Room Booking System"

# 4. 连接到 GitHub 仓库
git remote add origin https://github.com/your-username/study-room-booking.git

# 5. 推送到 GitHub
git branch -M main
git push -u origin main
```

### 步骤 2：部署后端

1. 在 Railway 部署后端
2. 获取后端 API URL：`https://your-backend.railway.app`

### 步骤 3：部署前端

1. 在前端配置后端 URL
2. 在 Vercel 部署前端
3. 设置环境变量 `VITE_API_BASE_URL`

### 步骤 4：配置 CORS

确保后端 `CorsConfig.java` 允许前端域名：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",
                    "https://your-app.vercel.app"  // 添加 Vercel 域名
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### 步骤 5：测试

1. 访问 Vercel 提供的前端 URL
2. 测试登录、预约等功能
3. 检查浏览器控制台是否有错误

## ⚙️ 环境变量配置

### 前端环境变量

在 Vercel/Netlify 中配置：

| 变量名 | 值 | 说明 |
|--------|-----|------|
| `VITE_API_BASE_URL` | `https://your-backend.railway.app/api` | 后端 API 地址 |

### 后端环境变量

在 Railway/Render 中配置：

| 变量名 | 值 | 说明 |
|--------|-----|------|
| `SPRING_PROFILES_ACTIVE` | `prod` | 激活生产配置 |
| `SERVER_PORT` | `8080` | 服务端口 |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://...` | 数据库 URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | 数据库用户名 |
| `SPRING_DATASOURCE_PASSWORD` | `xxx` | 数据库密码 |

## 🎯 生产环境优化

### 1. 数据库切换

**切换到 PostgreSQL**（推荐）

在 `pom.xml` 添加：

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

在 `application.yml` 添加 `prod` 配置：

```yaml
spring:
  profiles:
    active: @spring.profiles.active@
  
---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
```

### 2. 前端优化

```bash
# 构建时优化
npm run build

# 分析包大小
npm install -g vite-bundle-visualizer
vite-bundle-visualizer
```

### 3. 安全配置

- ✅ 使用 HTTPS（Vercel/Railway 自动提供）
- ✅ 配置 CORS 白名单
- ✅ 不要在代码中硬编码密钥
- ✅ 使用环境变量存储敏感信息

## ❓ 常见问题

### Q1：前端无法连接后端

**A**：检查：
1. 后端 URL 是否正确
2. CORS 配置是否包含前端域名
3. 后端是否正常运行

### Q2：Railway 部署失败

**A**：检查：
1. Dockerfile 是否正确
2. 构建日志中的错误信息
3. Java 版本是否为 17

### Q3：Vercel 构建失败

**A**：检查：
1. Node.js 版本是否符合要求
2. `package.json` 中的构建命令是否正确
3. 依赖是否完整

### Q4：部署后数据丢失

**A**：
- H2 内存数据库会在重启后清空
- 生产环境请使用 PostgreSQL
- Railway 提供免费 PostgreSQL 插件

### Q5：如何回滚部署

**Vercel**：
- 在 Deployments 页面选择之前的版本
- 点击 "Promote to Production"

**Railway**：
- 在 Deployments 页面选择之前的版本
- 点击 "Redeploy"

## 📊 费用估算

| 服务 | 免费额度 | 超出费用 |
|------|----------|----------|
| Vercel | 100GB 带宽/月 | 按量付费 |
| Railway | $5/月 | $0.000463/分钟 |
| Netlify | 100GB 带宽/月 | 按量付费 |
| Render | 750 小时/月 | $7/月起 |

**对于学习项目**：完全免费够用！

## 🎉 部署成功后

1. ✅ 前端 URL：`https://your-app.vercel.app`
2. ✅ 后端 URL：`https://your-backend.railway.app`
3. ✅ 在 README.md 中添加 Demo 链接
4. ✅ 添加部署状态徽章

```markdown
## Demo

- 🌐 前端: https://your-app.vercel.app
- 🔧 后端: https://your-backend.railway.app/api

[![Deploy on Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=...)
```

---

**祝部署顺利！** 🚀

如有问题，欢迎提 Issue！
