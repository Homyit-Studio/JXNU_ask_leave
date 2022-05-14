package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.VO.PageStudentVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.StudentUserVo;

import java.util.List;

public interface TeacherService {

    List<ClassInfoVO> getClassInfo();

    PageVo<StudentUserVo> getStudentsByClassId(PageStudentVo pageStudentVo);

    List<Long> getAllStudentNumber();


    List<ClassInfoVO> getAllClass();

}
