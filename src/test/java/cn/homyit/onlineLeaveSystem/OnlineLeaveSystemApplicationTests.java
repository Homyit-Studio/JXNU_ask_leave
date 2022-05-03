package cn.homyit.onlineLeaveSystem;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class OnlineLeaveSystemApplicationTests {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    SysStudentUserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void getPassword(){
        System.out.println(passwordEncoder.encode("042014"));

        System.out.println(passwordEncoder.matches("042014",
    "$2a$10$eMnulTBYY1jkOjcaWJYreeyqJKdWf/95E0TOx6qnMqypHluElo84K"));
    }

    @Test
    void getUser(){
        LambdaQueryWrapper<SysStudentUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudentUser::getStudentNumber,"202026202014");
        SysStudentUser user = userMapper.selectOne(wrapper);
    }

}
