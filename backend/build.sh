#!/bin/bash

# 自习室预约系统 - 构建脚本
# 此脚本确保使用Java 17进行编译

echo "======================================"
echo "自习室预约系统 - Maven构建"
echo "======================================"

# 设置Java 17环境
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "使用Java版本: $(java -version 2>&1 | head -n 1)"
echo ""

# 执行Maven构建
echo "开始构建..."
mvn clean install

# 检查构建结果
if [ $? -eq 0 ]; then
    echo ""
    echo "======================================"
    echo "✅ 构建成功!"
    echo "======================================"
    echo "JAR文件位置: target/study-room-booking-1.0.0.jar"
else
    echo ""
    echo "======================================"
    echo "❌ 构建失败,请检查错误信息"
    echo "======================================"
    exit 1
fi
