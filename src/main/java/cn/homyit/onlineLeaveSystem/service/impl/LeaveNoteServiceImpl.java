package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.myEnum.CompleteEnum;
import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OpinionEnum;
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
    private TeacherService teacherService;




    @Override
    public void insertNote(LeaveNote note) {
        long diff = note.getEndTime().getTime()-note.getStartTime().getTime();
        String diffStr = ""+diff/ (24 * 60 * 60 * 1000)+"天"+ diff/( 60 * 60 * 1000)%24+"时";
        note.setDays(diffStr);

        Integer days = Math.toIntExact(diff / (24 * 60 * 60 * 1000));
        if (days<3){
            note.setLevel(LevelEnum.INSTRUCTOR);
        }else if(days>=3&&days<7){
            note.setLevel(LevelEnum.SECRETARY);
        }else {
            note.setLevel(LevelEnum.DEAN);
        }
        leaveNoteMapper.insert(note);

    }

    @Override
    public PageVo<LeaveNote> selectNodeByGrade(SelectNotePageDTO selectNoteDTO) {
        Page<LeaveNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        wrapper.likeRight("student_number",selectNoteDTO.getGradeNumber())
                .orderByAsc("start_time");
        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
        if (completeEnum.equals(CompleteEnum.NO)){
            wrapper.apply("examine < level");
        }else {
            wrapper.apply("examine = level");
        }
        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public PageVo<LeaveNote> selectLeaveNote(SelectNotePageDTO selectNoteDTO) {
        Page<LeaveNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
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

        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        List<LeaveNote> list  = null;
        try {
            list = MyBeanUtils.deepCopy(iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("深拷贝异常");
        }
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());

    }

    //todo  根据用户角色来查找请假条

    @Override
    public PageVo<LeaveNote> selectNoteByRole(SelectNotePageDTO selectNoteDTO) {

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        Page<LeaveNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());

        //确定是审核还是未审核
        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
        if (completeEnum.equals(CompleteEnum.NO)){
            wrapper.apply("examine < level");
        }else {
            wrapper.apply("examine = level");
        }
        //确定用户角色
        LevelEnum role = loginUser.getUser().getRole();
        System.out.println(role+"-------------------");
        if (role.equals(LevelEnum.STUDENT)){
            wrapper.eq("student_number",loginUser.getUser().getStudentNumber());
        }else if (role.equals(LevelEnum.INSTRUCTOR)){
            List<Long> allStudentNumber = teacherService.getAllStudentNumber();
            wrapper.in("student_number",allStudentNumber);
        }else{
            //todo 大于辅导员,全看
        }

        wrapper.orderByAsc("start_time");

        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public LeaveNoteVo selectANote(Long id) {
        LeaveNote note = leaveNoteMapper.selectById(id);
        LeaveNoteVo leaveNoteVo = MyBeanUtils.copyBean(note, LeaveNoteVo.class);
        return leaveNoteVo;
    }

    @Override
    public void updateNote(UpdateNoteDTO updateNoteDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();
        Integer level = role.getValue();
        LeaveNote note = MyBeanUtils.copyBean(updateNoteDTO, LeaveNote.class);
        ExamineEnum examineEnum = updateNoteDTO.getExamineEnum();
        Integer examine =examineEnum.getValue();
        if (level<=examine){
            throw new RuntimeException("您已经同意");
        }

        //审核人意见
        //拒绝则不往下传递
        if (updateNoteDTO.getOpinionEnum().equals(OpinionEnum.NO)){
            note.setExamine(ExamineEnum.FAILURE);
        }else{
            //往下传递
            if(updateNoteDTO.getLevelEnum().getValue()-1 !=updateNoteDTO.getExamineEnum().getValue()){
                note.setExamine(ExamineEnum.getEumByCode(examineEnum.getValue().intValue()+1));
            }else{
                note.setExamine(ExamineEnum.SUCCESS);
            }

        }
        leaveNoteMapper.updateById(note);
    }


}
