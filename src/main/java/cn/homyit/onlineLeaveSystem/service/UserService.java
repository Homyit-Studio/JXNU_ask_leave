package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherUpdaterDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {

    HashMap<String,String> login(SysStudentUser user);

    void logout();

    StudentUserVo personInfo();

    void updatePWD(PasswordDTO passwordDTO);

    Map<String, Long> getAllLeaders();

    List<StudentUserVo> getNoteByStudentName(String username);

    void addUser(TeacherUpdaterDTO teacherUpdaterDTO);

    void updateUser(TeacherUpdaterDTO teacherUpdaterDTO);

    void deletedUser(TeacherUpdaterDTO teacherUpdaterDTO);

    void updateUserForStudent(StudentUpdateDTO studentUpdateDTO);
}
