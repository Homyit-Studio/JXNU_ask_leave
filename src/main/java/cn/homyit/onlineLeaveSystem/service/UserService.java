package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.StudentUserVo;

import java.util.HashMap;
import java.util.Map;

public interface UserService {

    HashMap<String,String> login(SysStudentUser user);

    void logout();

    StudentUserVo personInfo();

    void updatePWD(PasswordDTO passwordDTO);

    Map<String, Long> getAllLeaders();

}
