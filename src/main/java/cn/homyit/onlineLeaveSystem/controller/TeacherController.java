package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DTO.PageStudentDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.*;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 州牧
 * @description 班主任的接口，查询为主
 * @since 2022-05-04 14:53
 */
@RestController
@RequestMapping("/teacher")
//@PreAuthorize("hasAuthority('managing_students')")
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
    public Result<PageVo<StudentUserVo>> getStudentsByClassId(@RequestBody PageStudentDTO pageStudentDTO){
        PageVo<StudentUserVo> pageVo =teacherService.getStudentsByClassId(pageStudentDTO);
        return Result.success(pageVo);
    }

    /*获取所有班级*/
    @GetMapping("/getAllClass/{gradeId}")
    public Result<HashMap<String,Object>> getAllClass(@PathVariable Long gradeId){
        List<ClassInfoVO> list = teacherService.getAllClass(gradeId);
        HashMap<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("total",list.size());
        return Result.success(map);
    }

//    /*获取各个年级数量*/
//    @GetMapping("/getAllGradeId")
//    public Result getAllGradeId(){
//        List<ClassInfoVO> list = teacherService.getAllClass(gradeId);
//        Map<Long,Integer> map = new HashMap<>();
//        for (ClassInfoVO classInfoVO : list) {
//            Long id = classInfoVO.getGradeId();
//            map.put(id,map.getOrDefault(id,0)+1);
//        }
//        System.out.println(map);
//        return Result.success(map);
//    }

    @GetMapping("/deleteForGrade/{gradeId}")
    public Result deleteForGrade(@PathVariable Long gradeId){
        teacherService.deleteForGrade(gradeId);
        return Result.success();
    }






}
