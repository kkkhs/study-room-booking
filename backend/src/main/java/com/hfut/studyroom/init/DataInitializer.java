package com.hfut.studyroom.init;

import com.hfut.studyroom.entity.Building;
import com.hfut.studyroom.entity.Classroom;
import com.hfut.studyroom.entity.ClassroomOccupancy;
import com.hfut.studyroom.entity.Seat;
import com.hfut.studyroom.entity.User;
import com.hfut.studyroom.repository.BuildingRepository;
import com.hfut.studyroom.repository.ClassroomOccupancyRepository;
import com.hfut.studyroom.repository.ClassroomRepository;
import com.hfut.studyroom.repository.SeatRepository;
import com.hfut.studyroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 数据初始化类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final BuildingRepository buildingRepository;
    private final ClassroomRepository classroomRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final ClassroomOccupancyRepository occupancyRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        log.info("开始初始化数据...");
        
        // 1. 初始化教学楼
        initBuildings();
        
        // 2. 初始化教室
        initClassrooms();
        
        // 3. 初始化座位
        initSeats();
        
        // 4. 初始化用户
        initUsers();
        
        // 5. 初始化教室占用记录
        initClassroomOccupancies();
        
        log.info("数据初始化完成！");
    }
    
    /**
     * 初始化教学楼
     */
    private void initBuildings() {
        if (buildingRepository.count() > 0) {
            log.info("教学楼已存在，跳过初始化");
            return;
        }
        
        // 新安学堂
        Building xa = new Building();
        xa.setName("新安学堂");
        xa.setCode("XA");
        xa.setFloors(5);
        buildingRepository.save(xa);
        
        // 敬亭学堂
        Building jt = new Building();
        jt.setName("敬亭学堂");
        jt.setCode("JT");
        jt.setFloors(5);
        buildingRepository.save(jt);
        
        log.info("初始化教学楼：新安学堂、敬亭学堂");
    }
    
    /**
     * 初始化教室（每栋楼5层，每层30个教室）
     */
    private void initClassrooms() {
        if (classroomRepository.count() > 0) {
            log.info("教室已存在，跳过初始化");
            return;
        }
        
        for (Building building : buildingRepository.findAll()) {
            for (int floor = 1; floor <= 5; floor++) {
                for (int num = 1; num <= 30; num++) {
                    Classroom classroom = new Classroom();
                    classroom.setBuilding(building);
                    classroom.setFloor(floor);
                    classroom.setRoomNumber(floor + String.format("%02d", num));
                    classroom.setCapacity(100);
                    classroom.setStatus("OPEN");
                    classroomRepository.save(classroom);
                }
            }
        }
        
        log.info("初始化教室：2栋楼 × 5层 × 30个 = 300个教室");
    }
    
    /**
     * 初始化座位（每个教室100个座位，10行10列）
     */
    private void initSeats() {
        if (seatRepository.count() > 0) {
            log.info("座位已存在，跳过初始化");
            return;
        }
        
        int totalSeats = 0;
        for (Classroom classroom : classroomRepository.findAll()) {
            // 每个教室10行10列，共100个座位
            for (int row = 1; row <= 10; row++) {
                for (int col = 1; col <= 10; col++) {
                    Seat seat = new Seat();
                    seat.setClassroom(classroom);
                    seat.setSeatNumber(String.format("%d-%d", row, col));
                    seat.setRowNum(row);
                    seat.setColNum(col);
                    seatRepository.save(seat);
                    totalSeats++;
                }
            }
        }
        
        log.info("初始化座位：300个教室 × 100个座位 = {}个座位", totalSeats);
    }
    
    /**
     * 初始化用户
     */
    private void initUsers() {
        if (userRepository.count() > 0) {
            log.info("用户已存在，跳过初始化");
            return;
        }
        
        // 管理员账号
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("系统管理员");
        admin.setStudentId("A000000");
        admin.setPhone("18800000000");
        admin.setEmail("admin@hfut.edu.cn");
        admin.setRole("ADMIN");
        admin.setStatus("ACTIVE");
        admin.setViolationCount(0);
        userRepository.save(admin);
        
        // 测试用户1
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setRealName("张三");
        user1.setStudentId("2021001");
        user1.setPhone("18800000001");
        user1.setEmail("user1@hfut.edu.cn");
        user1.setRole("USER");
        user1.setStatus("ACTIVE");
        user1.setViolationCount(0);
        userRepository.save(user1);
        
        // 测试用户2
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setRealName("李四");
        user2.setStudentId("2021002");
        user2.setPhone("18800000002");
        user2.setEmail("user2@hfut.edu.cn");
        user2.setRole("USER");
        user2.setStatus("ACTIVE");
        user2.setViolationCount(0);
        userRepository.save(user2);
        
        log.info("初始化用户：admin（管理员）、user1、user2（测试用户）");
    }
    
    /**
     * 初始化教室占用记录（示例数据）
     */
    private void initClassroomOccupancies() {
        if (occupancyRepository.count() > 0) {
            log.info("教室占用记录已存在，跳过初始化");
            return;
        }
        
        // 获取所有教室
        List<Classroom> classrooms = classroomRepository.findAll();
        if (classrooms.isEmpty()) {
            return;
        }
        
        // 为今天和明天添加一些示例占用记录
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        // 今天的课程占用
        createOccupancy(classrooms.get(0), today, "08:00", "09:40", "高等数学", "COURSE", "张教授");
        createOccupancy(classrooms.get(1), today, "08:00", "09:40", "大学物理", "COURSE", "李教授");
        createOccupancy(classrooms.get(2), today, "10:00", "11:40", "线性代数", "COURSE", "王教授");
        createOccupancy(classrooms.get(0), today, "14:00", "15:40", "概率论", "COURSE", "赵教授");
        createOccupancy(classrooms.get(5), today, "14:00", "17:00", "学术会议", "MEETING", "科研处");
        
        // 明天的课程占用
        createOccupancy(classrooms.get(0), tomorrow, "08:00", "09:40", "高等数学", "COURSE", "张教授");
        createOccupancy(classrooms.get(1), tomorrow, "10:00", "11:40", "大学英语", "COURSE", "陈教授");
        createOccupancy(classrooms.get(3), tomorrow, "14:00", "15:40", "计算机原理", "COURSE", "刘教授");
        createOccupancy(classrooms.get(10), tomorrow, "09:00", "12:00", "教室维护", "MAINTENANCE", "后勤处");
        
        log.info("初始化教室占用记录：今天5条，明天4条");
    }
    
    /**
     * 创建教室占用记录辅助方法
     */
    private void createOccupancy(Classroom classroom, LocalDate date, String startTime, String endTime, 
                                  String reason, String type, String occupiedBy) {
        ClassroomOccupancy occupancy = new ClassroomOccupancy();
        occupancy.setClassroom(classroom);
        occupancy.setOccupancyDate(date);
        occupancy.setStartTime(LocalTime.parse(startTime));
        occupancy.setEndTime(LocalTime.parse(endTime));
        occupancy.setReason(reason);
        occupancy.setType(type);
        occupancy.setOccupiedBy(occupiedBy);
        occupancy.setStatus("SCHEDULED");
        occupancyRepository.save(occupancy);
    }
}
