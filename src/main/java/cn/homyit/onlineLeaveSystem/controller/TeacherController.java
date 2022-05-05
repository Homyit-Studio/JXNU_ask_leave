package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.SimpleNote;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassStudentVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 14:53
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    /*获取班级信息*/
    @GetMapping("/getClassInfo")
    public Result<List<ClassInfoVO>> getClassInfo(){
        List<ClassInfoVO> list = teacherService.getClassInfo();
        return Result.success(list);
    }

    /*获取某个班级的信息*/
    @GetMapping("/getStudentsByClassId/{classId}")
    public Result<PageVo<ClassStudentVo>> getStudentsByClassId(@PathVariable Long classId){
        PageVo<ClassStudentVo> pageVo =teacherService.getStudentsByClassId(classId);
        return Result.success(pageVo);
    }




}
