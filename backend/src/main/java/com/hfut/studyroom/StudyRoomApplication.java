package com.hfut.studyroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 自习室预约系统 - 主应用类
 * 
 * @author 合肥工业大学宣城校区
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling  // 启用定时任务
public class StudyRoomApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StudyRoomApplication.class, args);
        printStartupInfo();
    }
    
    private static void printStartupInfo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    自习室预约系统启动成功！");
        System.out.println("    合肥工业大学宣城校区");
        System.out.println("=".repeat(60));
        System.out.println("  📍 API地址: http://localhost:8080/api");
        System.out.println("  📍 H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("     JDBC URL: jdbc:h2:file:./data/studyroom");
        System.out.println("     Username: sa");
        System.out.println("     Password: (留空)");
        System.out.println("=".repeat(60) + "\n");
    }
}
