package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.VO.PageStudentVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.*;
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

    /*获取某个班级所有学生*/
    @PostMapping("/getStudentsByClassId")
    public Result<PageVo<StudentUserVo>> getStudentsByClassId(@RequestBody PageStudentVo pageStudentVo){
        PageVo<StudentUserVo> pageVo =teacherService.getStudentsByClassId(pageStudentVo);
        return Result.success(pageVo);
    }




}
