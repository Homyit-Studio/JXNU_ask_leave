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

    //todo 根据角色执行更形权限
    //todo 安全校验枚举值

    @PostMapping("/updateNote")
    public Result updateNote(@RequestBody UpdateNoteDTO updateNoteDTO){
        leaveNoteService.updateNote(updateNoteDTO);

        return Result.success();
    }



}
