package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SimpleNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.mapper.SimpleNoteMapper;
import cn.homyit.onlineLeaveSystem.myEnum.CompleteEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 州牧
 * @description
 * @since 2022-05-03 21:31
 */
@Service
public class LeaveNoteServiceImpl implements LeaveNoteService {

    @Autowired
    private LeaveNoteMapper leaveNoteMapper;
    @Autowired
    private SimpleNoteMapper simpleNoteMapper;

    @Autowired
    private TeacherService teacherService;

    /*生成请假条*/
    @Override
    public void insertNote(LeaveNote note) {
        leaveNoteMapper.insert(note);
        SimpleNote simpleNote = new SimpleNote();
        BeanUtils.copyProperties(note,simpleNote);
        long diff = note.getEndTime().getTime()-note.getStartTime().getTime();
        String diffStr = ""+diff/ (24 * 60 * 60 * 1000)+"天"+ diff/( 60 * 60 * 1000)%24+"时";
        simpleNote.setDays(diffStr);

        Integer days = Math.toIntExact(diff / (24 * 60 * 60 * 1000));
        if (days<3){
            simpleNote.setLevel(LevelEnum.INSTRUCTOR);
        }else if(days>=3&&days<7){
            simpleNote.setLevel(LevelEnum.SECRETARY);
        }else {
            simpleNote.setLevel(LevelEnum.DEAN);
        }

        simpleNoteMapper.insert(simpleNote);
    }

    @Override
    public PageVo<SimpleNote> selectSimpleNote(SelectNotePageDTO selectNoteDTO) {
        Page<SimpleNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());
        QueryWrapper<SimpleNote> wrapper = new QueryWrapper<>();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        wrapper.eq("student_number",loginUser.getUser().getStudentNumber())
                .orderByAsc("start_time");

        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
        if (completeEnum.equals(CompleteEnum.NO)){
            wrapper.apply("examine < level");
        }else {
            wrapper.apply("examine = level");
        }

        IPage<SimpleNote> iPage =simpleNoteMapper.selectPage(page,wrapper);
        List<SimpleNote> list  = null;
        try {
            list = MyBeanUtils.deepCopy(iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("深拷贝异常");
        }
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());

    }

    @Override
    public PageVo<SimpleNote> selectNoteByRole(SelectNotePageDTO selectNoteDTO) {

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        QueryWrapper<SimpleNote> wrapper = new QueryWrapper<>();
        Page<SimpleNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());

        //确定是审核还是未审核
        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
        if (completeEnum.equals(CompleteEnum.NO)){
            wrapper.apply("examine < level");
        }else {
            wrapper.apply("examine = level");
        }
        //确定用户角色
        LevelEnum role = loginUser.getUser().getRole();
        if (role.equals(LevelEnum.STUDENT)){
            wrapper.eq("student_number",loginUser.getUser().getStudentNumber());
        }else if (role.equals(LevelEnum.INSTRUCTOR)){
            List<Long> allStudentNumber = teacherService.getAllStudentNumber();
            wrapper.in("student_number",allStudentNumber);

        }else{
            //todo 大于辅导员
        }

        wrapper.orderByAsc("start_time");

        IPage<SimpleNote> iPage =simpleNoteMapper.selectPage(page,wrapper);
        List<SimpleNote> list  = null;
        try {
            list = MyBeanUtils.deepCopy(iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("深拷贝异常");
        }
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());
    }
}
