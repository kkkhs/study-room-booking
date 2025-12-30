#!/bin/bash

# 自习室预约系统 - 运行脚本 (macOS/Linux)
# 此脚本确保使用Java 17运行应用

echo "======================================"
echo "自习室预约系统 - 启动服务"
echo "======================================"
echo ""

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "❌ 未检测到Java，请先安装Java 17或更高版本"
    echo "macOS安装: brew install openjdk@17"
    echo "Ubuntu安装: sudo apt install openjdk-17-jdk"
    echo "或访问: https://adoptium.net/"
    exit 1
fi

# 设置Java 17环境（macOS）
if [[ "$OSTYPE" == "darwin"* ]]; then
    if command -v /usr/libexec/java_home &> /dev/null; then
        export JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null)
        if [ -z "$JAVA_HOME" ]; then
            echo "⚠️  未找到Java 17，使用当前Java版本"
        fi
    fi
fi

# 显示Java版本
echo "使用Java版本:"
java -version 2>&1 | head -n 1
echo ""

# 检查JAR文件是否存在
if [ ! -f "target/study-room-booking-1.0.0.jar" ]; then
    echo "❌ JAR文件不存在，请先运行 ./build.sh 构建项目"
    echo ""
    echo "运行命令: ./build.sh"
    exit 1
fi

# 启动应用
echo "启动应用..."
echo "后端服务地址: http://localhost:8080"
echo "按 Ctrl+C 停止服务"
echo "======================================"
echo ""

java -jar target/study-room-booking-1.0.0.jar
