@echo off
REM 自习室预约系统 - 构建脚本 (Windows)
REM 此脚本使用Maven进行编译

echo ======================================
echo 自习室预约系统 - Maven构建
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

REM 检查Maven是否安装
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未检测到Maven，请先安装Maven
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM 执行Maven构建
echo 开始构建...
call mvn clean install

REM 检查构建结果
if %errorlevel% equ 0 (
    echo.
    echo ======================================
    echo ✅ 构建成功!
    echo ======================================
    echo JAR文件位置: target\study-room-booking-1.0.0.jar
    echo.
) else (
    echo.
    echo ======================================
    echo ❌ 构建失败，请检查错误信息
    echo ======================================
    pause
    exit /b 1
)
