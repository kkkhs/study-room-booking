#!/bin/bash

# 自习室预约系统 - 前端开发服务器 (macOS/Linux)

echo "======================================"
echo "自习室预约系统 - 前端开发服务器"
echo "======================================"
echo ""

# 检查Node.js是否安装
if ! command -v node &> /dev/null; then
    echo "❌ 未检测到Node.js，请先安装Node.js 20或更高版本"
    echo "macOS安装: brew install node"
    echo "Ubuntu安装: curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash - && sudo apt install -y nodejs"
    echo "或访问: https://nodejs.org/"
    exit 1
fi

# 显示Node.js版本
echo "使用Node.js版本:"
node -v
echo ""

# 检查依赖是否安装
if [ ! -d "node_modules" ]; then
    echo "📦 正在安装依赖..."
    npm install
    if [ $? -ne 0 ]; then
        echo ""
        echo "❌ 依赖安装失败，请检查网络连接或使用国内镜像"
        echo "设置淘宝镜像: npm config set registry https://registry.npmmirror.com"
        exit 1
    fi
fi

echo ""
echo "🚀 启动开发服务器..."
echo "前端地址: http://localhost:5173"
echo "后端地址: http://localhost:8080/api"
echo ""
echo "按 Ctrl+C 停止服务"
echo "======================================"
echo ""

npm run dev
