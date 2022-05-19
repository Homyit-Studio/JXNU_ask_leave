package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherAddDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherUpdaterDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 州牧
 * @description
 * @since 2022-05-03 19:04
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiLog
    @PostMapping("/login")
    public Result login(@RequestBody @Validated SysStudentUser user){
        HashMap<String, String> map = userService.login(user);
        return Result.success(map);
    }

    @GetMapping("/logout")
    public Result logout(){
        userService.logout();
        return Result.success();
    }

//    @PreAuthorize("hasAuthority('managing_students')")
    @GetMapping("/personInfo")
    public Result<StudentUserVo> personInfo(){
        StudentUserVo  userInfo = userService.personInfo();
        return Result.success(userInfo);
    }

    @PreAuthorize("hasAuthority('managing_students')")
    @ApiLog
    @PostMapping("/updatePWD")
    public Result updatePWD(@RequestBody @Validated PasswordDTO passwordDTO){
        userService.updatePWD(passwordDTO);
        return Result.success();
    }
    @PreAuthorize("hasAuthority('managing_students')")
    /*获取负责人列表*/
    @GetMapping("/getAllLeaders")
    public Result getAllLeaders(){
        Map<String,Long> map = userService.getAllLeaders();
        return Result.success(map);
    }
//todo 返回的字段是否需要减少
    @PreAuthorize("hasAuthority('managing_students')")
    @GetMapping("/getNoteByStudentName")
    public Result<List<StudentUserVo>> getNoteByStudentName(@NotNull(message = "姓名不能为空") String username){
        List<StudentUserVo> list = userService.getNoteByStudentName(username);
        return Result.success(list);
    }

    //增加删除更新
    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/addUser")
    @ApiLog
    public Result addUser(@Validated @RequestBody TeacherAddDTO teacherAddDTO){
        userService.addUser(teacherAddDTO);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/updateUser")
    @ApiLog
    public Result updateUser( @RequestBody TeacherUpdaterDTO teacherUpdaterDTO){

        userService.updateUser(teacherUpdaterDTO);
        return Result.success();
    }


    @PostMapping("/updateUserForStudent")
    @ApiLog
    public Result updateUserForStudent( @RequestBody StudentUpdateDTO studentUpdateDTO){

        userService.updateUserForStudent(studentUpdateDTO);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/deletedUser")
    @ApiLog
    public Result deletedUser( @RequestBody TeacherUpdaterDTO TeacherUpdaterDTO){
        userService.deletedUser(TeacherUpdaterDTO);
        return Result.success();
    }


}
