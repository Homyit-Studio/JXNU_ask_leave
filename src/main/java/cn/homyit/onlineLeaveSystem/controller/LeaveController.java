package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.SimpleNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    //todo 加上事务，两个表更新
    @ApiLog
    @PostMapping("/ask")
    public Result ask(@RequestBody LeaveNote note){
        leaveNoteService.insertNote(note);
        return Result.success();
    }


//
    @PostMapping("/selectNote")
    public Result<PageVo<SimpleNote>> selectSimpleNote(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<SimpleNote> simpleNoteListVo = leaveNoteService.selectSimpleNote(selectNoteDTO);
        return Result.success(simpleNoteListVo);
    }
    //根据用户角色来执行不同的逻辑
    @ApiLog
    @PostMapping("/selectNoteByRole")
    public Result<PageVo<SimpleNote>> selectNoteByRole(@RequestBody SelectNotePageDTO selectNoteDTO){
        PageVo<SimpleNote> simpleNoteListVo = leaveNoteService.selectNoteByRole(selectNoteDTO);
        return Result.success(simpleNoteListVo);
    }

}
