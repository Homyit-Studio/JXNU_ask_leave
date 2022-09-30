package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserAddDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserUpdaterDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.entity.VO.TeacherUserVo;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;

import javax.servlet.http.HttpServletRequest;
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

    void addUser(UserAddDTO userAddDTODTO);

    void updateUser(UserUpdaterDTO userUpdaterDTO);

    void deletedUser(UserUpdaterDTO userUpdaterDTO);

    void updateUserForStudent(StudentUpdateDTO studentUpdateDTO);

    List<TeacherUserVo> getUserByRole(LevelEnum role);

    StudentUserVo getNoteByStudentNumber(Long studentNumber);


    void resetPasswordByEmail(Long studentNumber, HttpServletRequest request);

    void resetPasswordByTeacher(Long studentNumber);
}
