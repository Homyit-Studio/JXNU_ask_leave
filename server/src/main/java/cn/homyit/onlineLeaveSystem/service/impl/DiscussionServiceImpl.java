package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.*;
import cn.homyit.onlineLeaveSystem.entity.DTO.DiscussionDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PageDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.mapper.CommentMapper;
import cn.homyit.onlineLeaveSystem.mapper.DiscussionMapper;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.service.DiscussionService;
import cn.homyit.onlineLeaveSystem.util.ExcludeEmptyQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 16:21
 */
@Service
public class DiscussionServiceImpl implements DiscussionService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussionMapper discussionMapper;

    private SysStudentUser getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUser) authentication.getPrincipal()).getUser();
    }

    @Override
    public Long add(SysDiscussion discussion) {
        SysStudentUser user = getUser();
        discussion.setUserId(user.getId());
        discussion.setLevel(user.getRole().getValue());
        discussion.setUserName(user.getRole().getDesc()+":"+user.getUsername());
        discussionMapper.insert(discussion);
        return discussion.getId();
    }

    @Override
    public PageVo<SysDiscussion> selectPage(DiscussionDTO discussionDTO) {

        Page<SysDiscussion> page = new Page<>(discussionDTO.getPageNo(), discussionDTO.getPageSize());
        ExcludeEmptyQueryWrapper<SysDiscussion> wrapper = new ExcludeEmptyQueryWrapper<>();
        if (discussionDTO.getSpace().intValue() == 0){
            wrapper.eq("level",LevelEnum.STUDENT.getValue());
        }else if(discussionDTO.getSpace().intValue()- 1==0){

            wrapper.ne(false,"level",LevelEnum.STUDENT.getValue());

        }else{
            wrapper.eq("user_id",getUser().getId());
        }
        wrapper.orderByDesc("create_time");

        IPage<SysDiscussion> iPage  = discussionMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public void delete(Long discussionId) {
        SysStudentUser user = getUser();
        SysDiscussion sysDiscussion = discussionMapper.selectById(discussionId);
        System.out.println(user);

        if(sysDiscussion.getUserId().longValue() != user.getId().longValue()) {
            throw new BizException(ExceptionCodeEnum.DELETE_DIS_ERROR);
        }
        discussionMapper.deleteById(discussionId);
        //删除所有评论
        QueryWrapper<SysComment> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id",discussionId);
        commentMapper.delete(wrapper);
    }
}
