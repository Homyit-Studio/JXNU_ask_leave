package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysTeacherUser;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassStudentVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;

import java.util.List;

public interface TeacherService {

    List<ClassInfoVO> getClassInfo();

    PageVo<ClassStudentVo> getStudentsByClassId(Long classId);

    public List<Long> getAllStudentNumber();


}
