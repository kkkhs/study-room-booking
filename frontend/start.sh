#!/bin/bash

echo "======================================"
echo "自习室预约系统 - 前端开发服务器"
echo "======================================"

# 检查依赖是否安装
if [ ! -d "node_modules" ]; then
    echo "📦 正在安装依赖..."
    npm install
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
