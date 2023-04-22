package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.entity.DTO.*;
import cn.homyit.onlineLeaveSystem.entity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.log.UserLog;
import cn.homyit.onlineLeaveSystem.myEnum.ModuleEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OperationEnum;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author 州牧
 * @description 假条处理
 * @since 2022-05-03 21:17
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveNoteService leaveNoteService;

    @UserLog(module = ModuleEnum.NOTE,title = "申请请假",type = OperationEnum.ADD)
    @ApiLog
    @PostMapping("/ask")
    public Result insertNote(@Validated @RequestBody LeaveNote note){
        leaveNoteService.insertNote(note);
        return Result.success();
    }


    //根据学号获取
    @PostMapping("/selectNote")
    public Result<PageVo<LeaveNoteVo>> selectLeaveNote(@Validated @RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNoteVo> simpleNoteListVo = leaveNoteService.selectLeaveNote(selectNoteDTO);
        return Result.success(simpleNoteListVo);
    }

    //根据用户角色来执行不同的逻辑
    @PostMapping("/selectNoteByRole")
    public Result<PageVo<LeaveNoteVo>> selectNoteByRole(@Validated @RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNoteVo> noteListVo = leaveNoteService.selectNoteByRole(selectNoteDTO);
        return Result.success(noteListVo);
    }

    @GetMapping("/selectANote/{id}")
    public Result<LeaveNoteVo> selectANote(@PathVariable Long id){
        LeaveNoteVo leaveNoteVo = leaveNoteService.selectANote(id);
        return Result.success(leaveNoteVo);
    }
//根据学生信息查找假条
    @PostMapping("/selectNotesByStudentInfo")
    public Result<PageVo<LeaveNoteVo>> selectNotesByStudentInfo(@RequestBody FindNotesDTO findNotesDTO){
        PageVo<LeaveNoteVo> noteListVo = leaveNoteService.selectNotesByStudentInfo(findNotesDTO);
        return Result.success(noteListVo);
    }

    @PostMapping("/updateTheNote")
    public Result<LeaveNoteVo> updateTheNote( @RequestBody AllUpdateDTO allUpdateDTO){

        leaveNoteService.updateTheNote(allUpdateDTO);
        LeaveNoteVo leaveNoteVo = leaveNoteService.selectANote(allUpdateDTO.getId());
        return Result.success(leaveNoteVo);
    }

    @UserLog(module = ModuleEnum.NOTE,title = "审批假条",type = OperationEnum.MODIFY)
    @ApiLog
    @PostMapping("/updateNote")
    public Result updateNote(@Validated @RequestBody UpdateNoteDTO updateNoteDTO){
        leaveNoteService.updateNote(updateNoteDTO);

        return Result.success();
    }
    //批量处理假条
    //讲真，见证自己越来越没耐心，摆了个大的
    @PostMapping("/updateLotsNote")
    public Result updateLotsNote(@Validated @RequestBody ManyNotesDTO manyNotesDTO){
        leaveNoteService.updateLotsNote(manyNotesDTO);

        return Result.success();
    }

    @PostMapping("/selectNodeByGrade")
    public Result<PageVo<LeaveNoteVo>> selectNodeByGrade(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNoteVo> pageVo = leaveNoteService.selectNodeByGrade(selectNoteDTO);
        return Result.success(pageVo);
    }

    @UserLog(module = ModuleEnum.NOTE,title = "增加用户",type = OperationEnum.DELETE)
    @ApiLog
    @GetMapping("/deletedANote/{id}")
    public Result deletedANote(@PathVariable Long id){
        leaveNoteService.deletedANote(id);
        return Result.success();
    }

    /*首页个人管理班级报表处理*/

    @GetMapping("/allCountsForPerson")
    public Result<Map<String,Integer>> allCounts(){
        Map<String,Integer> map = leaveNoteService.allCounts();
        return  Result.success(map);
    }

    /*年级总览*/
    @PostMapping("/allCounts")
    public Result  allCountForGrade(@RequestBody TableTimeDTO tableTimeDTO){
        Map<String,Integer> map = leaveNoteService.allCountForGrade(tableTimeDTO);
        return Result.success(map);
    }


    @PostMapping("/allCountsFroGradeId")
    public Result  allCountForGradeId(@RequestBody TableTimeDTO tableTimeDTO){
        Map<String,Integer> map = leaveNoteService.allCountForGradeId(tableTimeDTO);
        return Result.success(map);
    }





}
