package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;

import java.util.HashMap;

public interface UserService {

    HashMap<String,String> login(SysStudentUser user);
}
