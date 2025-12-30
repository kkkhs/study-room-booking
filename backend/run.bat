@echo off
REM 自习室预约系统 - 运行脚本 (Windows)
REM 此脚本启动Spring Boot应用

echo ======================================
echo 自习室预约系统 - 启动服务
echo ======================================
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未检测到Java，请先安装Java 17或更高版本
    echo 下载地址: https://adoptium.net/
    pause
    exit /b 1
)

REM 显示Java版本
echo 使用Java版本:
java -version 2>&1 | findstr /C:"version"
echo.

REM 检查JAR文件是否存在
if not exist "target\study-room-booking-1.0.0.jar" (
    echo ❌ JAR文件不存在，请先运行 build.bat 构建项目
    echo.
    echo 运行命令: build.bat
    pause
    exit /b 1
)

REM 启动应用
echo 启动应用...
echo 后端服务地址: http://localhost:8080
echo 按 Ctrl+C 停止服务
echo ======================================
echo.

java -jar target\study-room-booking-1.0.0.jar
