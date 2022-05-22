package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.BackNoteVo;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.log.UserLog;
import cn.homyit.onlineLeaveSystem.myEnum.ModuleEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OperationEnum;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 22:31
 */
@RestController
@RequestMapping("/back")
//@PreAuthorize("hasAuthority('student')")
public class BackController {

    @Autowired
    private BackNoteService backNoteService;

    @GetMapping("/selectANote/{id}")
    public Result<BackNoteVo> selectANote(@PathVariable Long id){
        BackNoteVo backNoteVo = backNoteService.selectANote(id);
        return Result.success(backNoteVo);
    }

    @UserLog(module = ModuleEnum.NOTE,title = "销假",type = OperationEnum.MODIFY)
    @ApiLog
    @PostMapping("/updateNote")
    public Result updateNote(@Validated @RequestBody BackNoteDTO backNoteVoDTO){
        backNoteService.updateNote(backNoteVoDTO);
        return Result.success();
    }


}
