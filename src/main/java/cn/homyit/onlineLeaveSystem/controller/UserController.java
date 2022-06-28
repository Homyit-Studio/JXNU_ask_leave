package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserAddDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserUpdaterDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.entity.VO.TeacherUserVo;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.log.UserLog;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.ModuleEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OperationEnum;
import cn.homyit.onlineLeaveSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 州牧
 * @description 用户通用的接口，本应当将teacherController合并
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

    @GetMapping("/getNoteByStudentName")
    public Result<List<StudentUserVo>> getNoteByStudentName(@NotNull(message = "姓名不能为空") String username){
        List<StudentUserVo> list = userService.getNoteByStudentName(username);
        return Result.success(list);
    }

    @GetMapping("/getNoteByStudentNumber")
    public Result<StudentUserVo> getNoteByStudentNumber(@NotNull(message = "学号不能为空") Long studentNumber){
        StudentUserVo studentUserVo = userService.getNoteByStudentNumber(studentNumber);
        return Result.success(studentUserVo);


    }

    //增加删除更新
//    @UserLog(module = ModuleEnum.MANAGE_USER,title = "增加用户",type = OperationEnum.ADD)
    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/addUser")
    @ApiLog
    public Result addUser(@Validated @RequestBody UserAddDTO userAddDTO){
        userService.addUser(userAddDTO);
        return Result.success();
    }

    @UserLog(module = ModuleEnum.MANAGE_USER,title = "修改用户信息",type = OperationEnum.MODIFY)
    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/updateUser")
    @ApiLog
    public Result updateUser( @RequestBody UserUpdaterDTO userUpdaterDTO){

        userService.updateUser(userUpdaterDTO);
        return Result.success();
    }

    @UserLog(module = ModuleEnum.MANAGE_USER,title = "修改学生信息",type = OperationEnum.ADD)
    @PostMapping("/updateUserForStudent")
    @ApiLog
    public Result updateUserForStudent( @RequestBody StudentUpdateDTO studentUpdateDTO){

        userService.updateUserForStudent(studentUpdateDTO);
        return Result.success();
    }

    @UserLog(module = ModuleEnum.MANAGE_USER,title = "删除用户",type = OperationEnum.DELETE)
    @PreAuthorize("hasAuthority('managing_students')")
    @PostMapping("/deletedUser")
    @ApiLog
    public Result deletedUser( @RequestBody UserUpdaterDTO UserUpdaterDTO){
        userService.deletedUser(UserUpdaterDTO);
        return Result.success();
    }

    @GetMapping("/getUserByRole")
    public Result<List<TeacherUserVo>> getUserByRole(@RequestParam("role") @NotNull(message = "角色不能为空") LevelEnum role){

        List<TeacherUserVo> list = userService.getUserByRole(role);
        return Result.success(list);
    }


}
