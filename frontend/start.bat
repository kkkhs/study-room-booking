@echo off
REM 自习室预约系统 - 前端开发服务器 (Windows)

echo ======================================
echo 自习室预约系统 - 前端开发服务器
echo ======================================
echo.

REM 检查Node.js是否安装
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未检测到Node.js，请先安装Node.js 20或更高版本
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

REM 显示Node.js版本
echo 使用Node.js版本:
node -v
echo.

REM 检查依赖是否安装
if not exist "node_modules" (
    echo 📦 正在安装依赖...
    call npm install
    if %errorlevel% neq 0 (
        echo.
        echo ❌ 依赖安装失败，请检查网络连接或使用国内镜像
        echo 设置淘宝镜像: npm config set registry https://registry.npmmirror.com
        pause
        exit /b 1
    )
)

echo.
echo 🚀 启动开发服务器...
echo 前端地址: http://localhost:5173
echo 后端地址: http://localhost:8080/api
echo.
echo 按 Ctrl+C 停止服务
echo ======================================
echo.

npm run dev
