# 多阶段构建 - 第一阶段：构建
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 赋予执行权限
RUN chmod +x mvnw

# 下载依赖（利用 Docker 缓存）
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用（跳过测试以加快构建速度）
RUN ./mvnw clean package -DskipTests

# 多阶段构建 - 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 创建非 root 用户
RUN addgroup -g 1001 spring && adduser -D -u 1001 -G spring spring

# 从构建阶段复制 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 更改所有者
RUN chown spring:spring app.jar

# 创建数据目录并设置权限（用于H2数据库）
RUN mkdir -p data && chown spring:spring data

# 切换到非 root 用户
USER spring:spring

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar"]
