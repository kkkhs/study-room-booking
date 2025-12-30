#!/bin/bash

# 自习室预约系统 - 运行脚本
# 此脚本确保使用Java 17运行应用

echo "======================================"
echo "自习室预约系统 - 启动服务"
echo "======================================"

# 设置Java 17环境
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "使用Java版本: $(java -version 2>&1 | head -n 1)"
echo ""

# 检查JAR文件是否存在
if [ ! -f "target/study-room-booking-1.0.0.jar" ]; then
    echo "❌ JAR文件不存在,请先运行 ./build.sh 构建项目"
    exit 1
fi

# 启动应用
echo "启动应用..."
java -jar target/study-room-booking-1.0.0.jar
