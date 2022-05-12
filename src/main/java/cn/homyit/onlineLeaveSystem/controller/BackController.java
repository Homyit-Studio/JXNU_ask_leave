package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.BackNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 22:31
 */
@RestController
@RequestMapping("/back")
public class BackController {

    @Autowired
    private BackNoteService backNoteService;

    @GetMapping("/selectANote/{id}")
    public Result<BackNoteVo> selectANote(@PathVariable Long id){
        BackNoteVo backNoteVo = backNoteService.selectANote(id);
        return Result.success(backNoteVo);
    }

    @PostMapping("/updateNote")
    public Result updateNote(@RequestBody BackNoteDTO backNoteVoDTO){
        backNoteService.updateNote(backNoteVoDTO);
        return Result.success();
    }


}
