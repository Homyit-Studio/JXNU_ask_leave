package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.SysDiscussion;
import cn.homyit.onlineLeaveSystem.entity.DTO.DiscussionDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PageDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 州牧
 * @description 实现功能，er图，数据模式，10分钟内
 *
 * @since 2022-11-20 16:22
 */
@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @PostMapping("/add")
    public Result add(@RequestBody SysDiscussion discussion){
        Long discussId = discussionService.add(discussion);
        Map<String,Long> map = new HashMap<>();
        map.put("discussId",discussId);
        return Result.success(map);
    }

    @GetMapping("/delete")
    public Result delete(Long discussionId){
        discussionService.delete(discussionId);
        return Result.success();
    }

    @PostMapping("/selectPage")
    public Result<PageVo<SysDiscussion>> selectPage(@RequestBody DiscussionDTO discussionDTO){
        PageVo<SysDiscussion> res = discussionService.selectPage(discussionDTO);
        return Result.success(res);
    }

}
