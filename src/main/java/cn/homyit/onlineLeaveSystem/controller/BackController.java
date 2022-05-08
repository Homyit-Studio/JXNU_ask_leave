package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    //note.id

    @PostMapping("/create")
    public Result create(@RequestBody BackNoteDTO backNoteDTO){
        backNoteService.create(backNoteDTO);
        return Result.success();
    }

}
