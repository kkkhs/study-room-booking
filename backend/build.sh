#!/bin/bash

# 自习室预约系统 - 构建脚本 (macOS/Linux)
# 此脚本确保使用Java 17进行编译

echo "======================================"
echo "自习室预约系统 - Maven构建"
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

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ 未检测到Maven，请先安装Maven"
    echo "macOS安装: brew install maven"
    echo "Ubuntu安装: sudo apt install maven"
    echo "或访问: https://maven.apache.org/download.cgi"
    exit 1
fi

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
    echo ""
else
    echo ""
    echo "======================================"
    echo "❌ 构建失败，请检查错误信息"
    echo "======================================"
    exit 1
fi
