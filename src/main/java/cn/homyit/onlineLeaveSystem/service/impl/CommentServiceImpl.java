package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.entity.DO.SysComment;
import cn.homyit.onlineLeaveSystem.entity.DO.SysDiscussion;
import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.CommentPageDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.GetSonDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.CommentVO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.mapper.CommentMapper;
import cn.homyit.onlineLeaveSystem.service.CommentService;
import cn.homyit.onlineLeaveSystem.util.ExcludeEmptyQueryWrapper;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 17:10
 */
@Service
public class CommentServiceImpl implements CommentService {

    private SysStudentUser getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUser) authentication.getPrincipal()).getUser();
    }

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void add(SysComment comment) {
        SysStudentUser user = getUser();
        comment.setUserId(user.getId());
        comment.setUserName(user.getRole().getDesc()+":"+user.getUsername());
//        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    @Override
    public void delete(Long commentId) {
        SysStudentUser user = getUser();
        SysComment sysComment = commentMapper.selectById(commentId);
        if(sysComment.getUserId().longValue() != user.getId().longValue()){
            //不能删除别人的评论
            throw  new BizException(ExceptionCodeEnum.DELETE_COM_ERROR);
        }
        deleteRecursion(commentId);
    }

    public void deleteRecursion(Long commentId){
        //选出子评论
        QueryWrapper<SysComment> wrapper = new QueryWrapper<>();
        wrapper.eq("reply_comment_id",commentId);
        List<SysComment> sysComments = commentMapper.selectList(wrapper);

        for (SysComment sysComment : sysComments) {
            //递归删除儿子
            deleteRecursion(sysComment.getId());
        }
        //删除自己
        commentMapper.deleteById(commentId);
    }

    @Override
    public PageVo<CommentVO> get(CommentPageDTO commentDTO) {
        Page<SysComment> page = new Page<>(commentDTO.getPageNo(),commentDTO.getPageSize());
        QueryWrapper<SysComment> wrapper = new QueryWrapper<>();
        wrapper.isNull("root_comment_id")
                .eq("discussion_id",commentDTO.getDiscussionId());
        if(commentDTO.getParentOpinion() == 1){
            wrapper.orderByAsc("create_time");
        }else{
            wrapper.orderByDesc("create_time");
        }
        IPage<SysComment> iPage =commentMapper.selectPage(page,wrapper);
        List<SysComment> records = iPage.getRecords();
        List<CommentVO> commentVOS = MyBeanUtils.copyList(records, CommentVO.class);



        //获取子评论


        for (CommentVO commentVO : commentVOS) {

            Long rootCommentId = commentVO.getId();
            QueryWrapper<SysComment> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("root_comment_id",rootCommentId);
            if(commentDTO.getSonOpinion() == 1){
                wrapper1.orderByAsc("create_time");
            }else{
                wrapper1.orderByDesc("create_time");
            }

            Page<SysComment> pageSon = 
                    new Page<>(commentDTO.getSonPageNo(),commentDTO.getSonPageSize());
            IPage<SysComment> page1 = commentMapper.selectPage(pageSon, wrapper1);
            List<SysComment> son = page1.getRecords();
            List<CommentVO> sonList = MyBeanUtils.copyList(son, CommentVO.class);

            commentVO.setSon(new PageVo<>(sonList,page1.getTotal(),page1.getPages()));
        }
        return new PageVo<>(commentVOS,iPage.getTotal(),iPage.getPages());
    }

    @Override
    public PageVo<SysComment> getSon(GetSonDTO getSonDTO) {
        QueryWrapper<SysComment> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("root_comment_id",getSonDTO.getRootCommentId())
                .eq("discussion_id",getSonDTO.getDiscussionId());
        if(getSonDTO.getSonOpinion() == 1){
            wrapper1.orderByAsc("create_time");
        }else {
            wrapper1.orderByDesc("create_time");
        }
        Page<SysComment> page = new Page<>(getSonDTO.getPageNo(),getSonDTO.getPageSize());

        IPage<SysComment> page1 = commentMapper.selectPage(page, wrapper1);
        return new PageVo<>(page1.getRecords(), page1.getTotal(),page1.getPages());
    }


}
