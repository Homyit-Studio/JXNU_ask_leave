package cn.homyit.onlineLeaveSystem;

import cn.homyit.onlineLeaveSystem.log.EnableApiLog;
import cn.homyit.onlineLeaveSystem.log.EnableUserLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@EnableApiLog
//@EnableUserLog
@MapperScan("cn.homyit.onlineLeaveSystem.mapper")
@SpringBootApplication
public class OnlineLeaveSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineLeaveSystemApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
