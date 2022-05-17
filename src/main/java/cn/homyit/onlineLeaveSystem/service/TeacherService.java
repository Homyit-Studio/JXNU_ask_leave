package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DTO.PageStudentDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;

import java.util.List;

public interface TeacherService {

    List<ClassInfoVO> getClassInfo();

    PageVo<StudentUserVo> getStudentsByClassId(PageStudentDTO pageStudentDTO);

    List<Long> getAllStudentNumber();


    List<ClassInfoVO> getAllClass();

}
