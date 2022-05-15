package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //ok
    @ApiLog
    @PostMapping("/ask")
    public Result insertNote(@RequestBody LeaveNote note){
        leaveNoteService.insertNote(note);
        return Result.success();
    }


    //根据学号获取
    @PostMapping("/selectNote")
    public Result<PageVo<LeaveNote>> selectLeaveNote(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNote> simpleNoteListVo = leaveNoteService.selectLeaveNote(selectNoteDTO);
        return Result.success(simpleNoteListVo);
    }

    //根据用户角色来执行不同的逻辑
    @ApiLog
    @PostMapping("/selectNoteByRole")
    public Result<PageVo<LeaveNote>> selectNoteByRole(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNote> noteListVo = leaveNoteService.selectNoteByRole(selectNoteDTO);
        return Result.success(noteListVo);
    }

    @GetMapping("/selectANote/{id}")
    public Result<LeaveNoteVo> selectANote(@PathVariable Long id){
        LeaveNoteVo leaveNoteVo = leaveNoteService.selectANote(id);
        return Result.success(leaveNoteVo);
    }


    @PostMapping("/updateNote")
    public Result updateNote(@RequestBody UpdateNoteDTO updateNoteDTO){
        leaveNoteService.updateNote(updateNoteDTO);

        return Result.success();
    }

    @PostMapping("/selectNodeByGrade")
    public Result<PageVo<LeaveNote>> selectNodeByGrade(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<LeaveNote> pageVo = leaveNoteService.selectNodeByGrade(selectNoteDTO);
        return Result.success(pageVo);
    }

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
    @GetMapping("/allCounts")
    public Result  allCountForGrade(){
        Map<String,Integer> map = leaveNoteService.allCountForGrade();
        return Result.success(map);
    }

    @GetMapping("/allCounts/{gradeId}")
    public Result  allCountForGradeId(@PathVariable Long gradeId){
        Map<String,Integer> map = leaveNoteService.allCountForGradeId(gradeId);
        return Result.success(map);
    }





}
