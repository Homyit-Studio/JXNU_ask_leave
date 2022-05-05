package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.UserInfo;

import java.util.HashMap;

public interface UserService {

    HashMap<String,String> login(SysStudentUser user);

    void logout();

    UserInfo personInfo();

    void updatePWD(PasswordDTO passwordDTO);

}
