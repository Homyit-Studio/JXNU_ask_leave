package cn.homyit.onlineLeaveSystem;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.mapper.*;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootTest
class OnlineLeaveSystemApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysStudentUserMapper userMapper;

    @Autowired
    LeaveNoteMapper  leaveNoteMapper;
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
        wrapper.eq(SysStudentUser::getStudentNumber,"202026202013");
        SysStudentUser user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    void testMeiju(){
        LeaveNote note = new LeaveNote();
        note.setStudentNumber(202026202013L);
        note.setMajorAndClass("20级计科一班");
        note.setStartTime(new Date());
        note.setEndTime(new Date());
        note.setUsername("jkl");
        note.setDepart(LeaveEnum.YES);
        note.setDestination("20级计科一班");
        note.setDormitoryNumber("20级计科一班");
        note.setWay("20级计科一班");
        note.setPhoneNumber("20级计科一班");
        note.setReason("20级计科一班");
//        leaveNoteMapper.insert(note);
//
//        SimpleNote simpleNote = new SimpleNote();
//        BeanUtils.copyProperties(note,simpleNote);
//        System.out.println(simpleNote);
    }

    @Test
    void test01(){
        SysStudentUser user = new SysStudentUser();
        user.setStudentNumber(202026202012L);
        user.setPassword("123456");
        user.setPhoneNumber("123456");
        user.setUsername("123456");
        user.setSex(SexEnum.MAN);
        userMapper.insert(user);
    }

//    @Autowired
//    SimpleNoteMapper simpleNoteMapper;
//    @Test
//    void test02(){
//        SimpleNote simpleNote = new SimpleNote();
//        simpleNote.setDepart(LeaveEnum.YES);
//        simpleNote.setDays("123456h");
//        simpleNote.setMajorAndClass("123456");
////        simpleNote.setStartTime(new Date());
//        simpleNote.setUsername("123456");
//        simpleNote.setStudentNumber(202026202014L);
//        simpleNoteMapper.insert(simpleNote);
//    }

//    @Test
//    void Test03(){
//        Page<SimpleNote> page = new Page<>(1,10);
//        IPage<SimpleNote> page1 = simpleNoteMapper.selectPage(page, null);
//        List<SimpleNote> records = page1.getRecords();
//        System.out.println(page1.getTotal());
//        System.out.println(page1.getSize());
//        for (SimpleNote record : records) {
//            System.out.println(record);
//        }
//    }

    @Autowired
    private ClassInfoMapper classInfoMapper;
    @Test
    void Test04(){
        List<ClassInfoVO> classInfoVOS = classInfoMapper.selectClassInfoByNumber(10086L);
        for (ClassInfoVO classInfoVO : classInfoVOS) {
            System.out.println(classInfoVO);
        }
    }
//
//    @Autowired
//    private ClassStudentMapper  classStudentMapper;
//    @Test
//    void Test05(){
//
//        Page<SysStudentClass> page = new Page<>(1,10);
//        QueryWrapper<SysStudentClass> wrapper = new QueryWrapper<>();
//        wrapper.eq("class_id",1);
//        wrapper.orderByAsc("student_number");
//
//        IPage<SysStudentClass> page1 = classStudentMapper.selectPage(page, wrapper);
//        List<SysStudentClass> records = page1.getRecords();
//        for (SysStudentClass record : records) {
//            System.out.println(record);
//        }
//    }

    @Test
    void test06(){
        LevelEnum role = LevelEnum.DEAN;
        System.out.println(role.equals(LevelEnum.DEAN));
        System.out.println(role.equals(LevelEnum.LOOK));
    }
    @Autowired
    TeacherService teacherService;

    @Test
    void test07(){
        QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("major_and_class","20级计算机科学与技术一班")
                .select("id");
        SysStudentClassInfo sysStudentClassInfo = classInfoMapper.selectOne(wrapper);
        System.out.println(sysStudentClassInfo);
    }



}
