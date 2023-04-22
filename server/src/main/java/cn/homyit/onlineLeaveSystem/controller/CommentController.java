package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.SysComment;
import cn.homyit.onlineLeaveSystem.entity.DTO.CommentPageDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.GetSonDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.CommentVO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 16:39
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody SysComment comment){
        commentService.add(comment);
        return Result.success();
    }

    @GetMapping("/delete")
    public Result delete(Long commentId){
        commentService.delete(commentId);
        return Result.success();
    }

    @PostMapping("/get")
    public Result<PageVo<CommentVO>> get(@RequestBody CommentPageDTO commentDTO){
        PageVo<CommentVO> res = commentService.get(commentDTO);
        return Result.success(res);
    }

    @PostMapping("/getSon")
    public Result<PageVo<SysComment>> getSon(@RequestBody GetSonDTO getSonDTO){
        System.out.println(getSonDTO);
        PageVo<SysComment> page = commentService.getSon(getSonDTO);
        return Result.success(page);
    }
//    public Result

}
