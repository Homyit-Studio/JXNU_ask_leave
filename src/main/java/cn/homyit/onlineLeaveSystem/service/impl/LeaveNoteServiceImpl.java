package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.myEnum.*;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author 州牧
 * @description
 * @since 2022-05-03 21:31
 */

//todo 假条的过期处理
@Slf4j
@Service
public class LeaveNoteServiceImpl implements LeaveNoteService {

    @Autowired
    private LeaveNoteMapper leaveNoteMapper;


    @Autowired
    private TeacherService teacherService;

    @Autowired
    private BackNoteService backNoteService;



    @Override
    public void insertNote(LeaveNote note) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        SysStudentUser user = loginUser.getUser();
        long diff = note.getEndTime().getTime()-note.getStartTime().getTime();
        String diffStr = ""+diff/ (24 * 60 * 60 * 1000)+"天"+ diff/( 60 * 60 * 1000)%24+"时";
        note.setDays(diffStr);
        note.setStudentNumber(user.getStudentNumber());
        note.setMajorAndClass(user.getMajorAndClass());
        note.setDormitoryNumber(user.getBuildingNumber()+user.getDormitoryNumber());
        note.setUsername(user.getUsername());
        note.setPhoneNumber(user.getPhoneNumber());

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
                .orderByDesc("start_time");
        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
        if (completeEnum.equals(CompleteEnum.NO)){
            wrapper.ne("examine",ExamineEnum.WAIT_REPORT)
                    .ne("examine",ExamineEnum.FAILURE);
        }else {
            wrapper.in("examine",
                    Arrays.asList(ExamineEnum.WAIT_REPORT.getValue(),ExamineEnum.FAILURE.getValue()));
        }
        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public void deletedANote(Long id) {
        //删除假条
        leaveNoteMapper.deleteById(id);
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
            wrapper.ne("examine",ExamineEnum.WAIT_REPORT)
                    .ne("examine",ExamineEnum.FAILURE);
        }else {
            wrapper.in("examine",
                    Arrays.asList(ExamineEnum.WAIT_REPORT.getValue(),ExamineEnum.FAILURE.getValue()));
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


    @Override
    public PageVo<LeaveNote> selectNoteByRole(SelectNotePageDTO selectNoteDTO) {

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        Page<LeaveNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());
        //获取用户角色
        LevelEnum role = loginUser.getUser().getRole();
        //获取用户审核权限
        ExamineEnum examineRole = ExamineEnum.getEumByCode(role.getValue() - 1);
        //只获取一种类型的假条
        ExamineEnum examineEnum = selectNoteDTO.getExamineEnum();
        if (examineEnum.equals(ExamineEnum.PROCESSING)){
            wrapper.eq("examine",examineRole);
        }else {
            wrapper.eq("examine",examineEnum);
        }

        if (role.equals(LevelEnum.STUDENT)){
            wrapper.eq("student_number",loginUser.getUser().getStudentNumber());

        }else if (role.equals(LevelEnum.INSTRUCTOR)){
            List<Long> allStudentNumber = teacherService.getAllStudentNumber();
            wrapper.in("student_number",allStudentNumber);
        }else if (role.equals(LevelEnum.SECRETARY)){
            wrapper.eq("leader_number",loginUser.getUser().getStudentNumber());
        }

        wrapper.orderByDesc("start_time");
        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public LeaveNoteVo selectANote(Long id) {
        LeaveNote note = leaveNoteMapper.selectById(id);
        LeaveNoteVo leaveNoteVo = MyBeanUtils.copyBean(note, LeaveNoteVo.class);
        return leaveNoteVo;
    }

    //上级可跨越下级直接通过请求

    @Override
    public void updateNote(UpdateNoteDTO updateNoteDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();
        Integer level = role.getValue();

        //根据登录角色获取考核等级（并不是假条此时的等级而是说审核人的等级）
        ExamineEnum examineEnum =ExamineEnum.
                getEumByCode(LevelEnum.getEumByCode(level).getValue()-1);
        LeaveNote note = MyBeanUtils.copyBean(updateNoteDTO, LeaveNote.class);

        Integer examine =examineEnum.getValue();
        if (level<=examine){
            throw new RuntimeException("您已经同意");
        }
        //审核人意见
        //拒绝则不往下传递
        if (updateNoteDTO.getOpinionEnum().equals(OpinionEnum.NO)){
            note.setExamine(ExamineEnum.FAILURE);
        }else{
            //同意往下传递
            if(updateNoteDTO.getLevelEnum().getValue()-1 !=examine){
                note.setExamine(ExamineEnum.getEumByCode(examineEnum.getValue().intValue()+1));
                if (role.equals(LevelEnum.INSTRUCTOR)){
                    //手动指定负责人
                    note.setLeaderNumber(updateNoteDTO.getLeaderNumber());
                }

                //院长审批后也直接生成请假条
                if(role.equals(LevelEnum.DEAN)){
                    //生成销假条
                    BackNote backNote = MyBeanUtils.copyBean(note, BackNote.class);

                    //创建时学生仍然在学校
                    backNote.setDepart(LeaveEnum.NO);
                    //插入销假条表
                    backNoteService.insertNote(backNote);
                }
            }else{

                note.setExamine(ExamineEnum.WAIT_REPORT);

                //生成销假条
                BackNote backNote = MyBeanUtils.copyBean(note, BackNote.class);

                //创建时学生仍然在学校
                backNote.setDepart(LeaveEnum.NO);
                //插入销假条表
                backNoteService.insertNote(backNote);

            }

        }
        leaveNoteMapper.updateById(note);
    }

  //假条分为待处理，已传递，已销假，未销假，假条过期，销假过期
    @Override
    public Map<String,Integer>  allCounts() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();
        List<Long> allStudentNumber = teacherService.getAllStudentNumber();
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        wrapper.in("student_number",allStudentNumber);

        //待本人审核
        Integer roleCount = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",
                        ExamineEnum.getEumByCode(role.getValue().intValue()-1))
                .in("student_number",allStudentNumber)
        );
        //待下一级审核
        Integer transmit = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().apply("examine = "+(role.getValue().intValue()))
                        .in("student_number",allStudentNumber)
        );
        //销假完成
        Integer process = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.PROCESSED)
                .in("student_number",allStudentNumber)
        );
        //待销假
        Integer waitReport = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.WAIT_REPORT)
                .in("student_number",allStudentNumber)
        );
        //申请过期
        Integer applyExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.APPLY_EXPIRED)
                        .in("student_number",allStudentNumber)
        );
        //销假过期
        Integer reportExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.REPORT_EXPIRED)
                        .in("student_number",allStudentNumber)
        );

       log.info(" roleCount{},transmit{},process{},waitReport{},applyExpired{},reportExpired{}",roleCount,transmit,process,waitReport,applyExpired,reportExpired);

        Map<String,Integer> map  = new HashMap<>();
        map.put("roleCount",roleCount);
        map.put("transmit",transmit);
        map.put("process",process);
        map.put("waitReport",waitReport);
        map.put("applyExpired",applyExpired);
        map.put("reportExpired",reportExpired);

        return map;

    }

    @Override
    public void checkExpired() {
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        wrapper.lt("end_time",new Timestamp(new Date().getTime()))
                .eq("examine",ExamineEnum.WAIT_REPORT);
        LeaveNote note = new LeaveNote();
        note.setExamine(ExamineEnum.REPORT_EXPIRED);
        leaveNoteMapper.update(note,wrapper);

        QueryWrapper<LeaveNote> wrapper1 = new QueryWrapper<>();
        wrapper1.lt("end_time",new Timestamp(new Date().getTime()))
                .lt("examine",ExamineEnum.WAIT_REPORT);
        LeaveNote note1 = new LeaveNote();
        note1.setExamine(ExamineEnum.APPLY_EXPIRED);
        leaveNoteMapper.update(note1,wrapper1);
    }

    @Override
    public List<LeaveNote> selectNoteToDownload(DownloadNoteDTO downloadNoteDTO) {
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        wrapper.gt("start_time",downloadNoteDTO.getStartTime())
                .lt("end_time",downloadNoteDTO.getEndTime())
                .eq("student_number",downloadNoteDTO.getStudentNumber())
                .eq("grade",downloadNoteDTO.getGrade())
                .eq("class_id",downloadNoteDTO.getClassId());
        List<LeaveNote> leaveNotes = leaveNoteMapper.selectList(wrapper);
//        for (LeaveNote leaveNote : leaveNotes) {
//            log.info("假条如{}",leaveNote);
//        }
        return leaveNotes;
    }


}
