package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.eneity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result login(@RequestBody SysStudentUser user){
        HashMap<String, String> map = userService.login(user);
        return Result.success(map);
    }

    @GetMapping("/logout")
    public Result logout(){
        userService.logout();
        return Result.success();
    }

    @GetMapping("/personInfo")
    public Result<StudentUserVo> personInfo(){
        StudentUserVo  userInfo = userService.personInfo();
        return Result.success(userInfo);
    }

    @ApiLog
    @PostMapping("/updatePWD")
    public Result updatePWD(@RequestBody PasswordDTO passwordDTO){
        userService.updatePWD(passwordDTO);
        return Result.success();
    }

    /*获取负责人列表*/
    @GetMapping("/getAllLeaders")
    public Result getAllLeaders(){
        Map<String,Long> map = userService.getAllLeaders();
        return Result.success(map);
    }
//todo 返回的字段是否需要减少
    @GetMapping("/getNoteByStudentName")
    public Result<List<StudentUserVo>> getNoteByStudentName(String username){
        List<StudentUserVo> list = userService.getNoteByStudentName(username);
        return Result.success(list);

    }



}
