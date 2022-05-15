package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.*;
import cn.homyit.onlineLeaveSystem.eneity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.mapper.ImageMapper;
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
    private ImageMapper imageMapper;

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
        //学号肯定不行

       wrapper.orderByDesc("start_time")
               .eq("grade_id",selectNoteDTO.getGradeId());

       if (ExamineEnum.PROCESSING.equals(selectNoteDTO.getExamineEnum())){
           wrapper.in("examine", Arrays.asList(ExamineEnum.INSTRUCTOR.getValue(),
               ExamineEnum.SECRETARY.getValue(),ExamineEnum.DEAN.getValue()));
       }else {
           wrapper.eq("examine",selectNoteDTO.getExamineEnum());
       }
        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        return new PageVo<>(iPage.getRecords(),iPage.getTotal(),iPage.getPages());
    }

    @Override
    public void deletedANote(Long id) {
        //删除假条，未生成销假条之前
        leaveNoteMapper.deleteById(id);
    }



    //根据学号获取假条
    @Override
    public PageVo<LeaveNote> selectLeaveNote(SelectNotePageDTO selectNoteDTO) {
        Page<LeaveNote> page = new Page<>(selectNoteDTO.getPageNo(),selectNoteDTO.getPageSize());
        QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        wrapper.eq("student_number",loginUser.getUser().getStudentNumber())
                .orderByAsc("start_time");
        if(selectNoteDTO.getExamineEnum().equals(ExamineEnum.PROCESSING)){
            wrapper.in("examine",Arrays.asList(ExamineEnum.DEAN.getValue(),ExamineEnum.INSTRUCTOR.getValue(),ExamineEnum.SECRETARY.getValue()));
        }else{
            wrapper.eq("examine",selectNoteDTO.getExamineEnum());
        }
//        wrapper.eq()
//        CompleteEnum completeEnum = selectNoteDTO.getCompleteEnum();
//        if (completeEnum.equals(CompleteEnum.NO)){
//            wrapper.ne("examine",ExamineEnum.WAIT_REPORT)
//                    .ne("examine",ExamineEnum.FAILURE);
//        }else {
//            wrapper.in("examine",
//                    Arrays.asList(ExamineEnum.WAIT_REPORT.getValue(),ExamineEnum.FAILURE.getValue()));
//        }

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
        ExamineEnum examineRole = ExamineEnum.getEumByCode(role.getValue());
        //只获取一种类型的假条
        ExamineEnum examineEnum = selectNoteDTO.getExamineEnum();
        if (examineEnum.equals(ExamineEnum.PROCESSING)){
            //待本人审核
            //todo 优化，逻辑搞错了
            if (role.equals(LevelEnum.STUDENT)){
                wrapper.eq("examine",examineRole);
            }else {
                wrapper.eq("examine",role.getValue()-1);
            }

        }else if(examineEnum.equals(ExamineEnum.TRANSMIT)){
            //todo 还得记录是谁传递的吗？老子不干了，又要改表
            wrapper.eq("examine",role.getValue().intValue());
//            Long studentNumber = loginUser.getUser().getStudentNumber();
//            String roleId =
        } else {
            wrapper.eq("examine",examineEnum);
        }

        if (role.equals(LevelEnum.STUDENT)){
            wrapper.eq("student_number",loginUser.getUser().getStudentNumber());

        }else if (role.equals(LevelEnum.INSTRUCTOR)){
            List<Long> allStudentNumber = teacherService.getAllStudentNumber();
            wrapper.in("student_number",allStudentNumber);
        }else if (role.equals(LevelEnum.SECRETARY)){
            //todo  不太想改了
            wrapper.like("leader_number",loginUser.getUser().getStudentNumber());
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

    //上级可跨越下级直接通过请求，只限于请假

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
        if (role.equals(LevelEnum.LOOK)){
            throw new RuntimeException("您无管理班级");
        }
        //待本人审核
        QueryWrapper<LeaveNote> wrapper1 = new QueryWrapper<LeaveNote>().eq("examine",
                ExamineEnum.getEumByCode(role.getValue().intValue() - 1))
                .like("leader_number",loginUser.getUser().getStudentNumber());

        //待下一级审核
        QueryWrapper<LeaveNote> wrapper2 = new QueryWrapper<LeaveNote>().
                apply("examine = " + (role.getValue().intValue()));

        //销假完成
        QueryWrapper<LeaveNote> wrapper3 = new QueryWrapper<LeaveNote>().
                eq("examine", ExamineEnum.PROCESSED);

        //待销假
        QueryWrapper<LeaveNote> wrapper4 = new QueryWrapper<LeaveNote>().
                eq("examine", ExamineEnum.WAIT_REPORT);

        //申请过期
        QueryWrapper<LeaveNote> wrapper5 = new QueryWrapper<LeaveNote>().
                eq("examine", ExamineEnum.APPLY_EXPIRED);

        //销假过期
        QueryWrapper<LeaveNote> wrapper6 = new QueryWrapper<LeaveNote>().
                eq("examine", ExamineEnum.REPORT_EXPIRED);

        //已拒绝
        QueryWrapper<LeaveNote> wrapper7 = new QueryWrapper<LeaveNote>().
                eq("examine", ExamineEnum.FAILURE);

        if (role.equals(LevelEnum.INSTRUCTOR)){
            List<Long> allStudentNumber = teacherService.getAllStudentNumber();
            QueryWrapper<LeaveNote> wrapper = new QueryWrapper<>();
            wrapper.in("student_number",allStudentNumber);
            wrapper1.in("student_number", allStudentNumber);
            wrapper2.in("student_number", allStudentNumber);
            wrapper3.in("student_number", allStudentNumber);
            wrapper4.in("student_number", allStudentNumber);
            wrapper5.in("student_number", allStudentNumber);
            wrapper6.in("student_number", allStudentNumber);
            wrapper7.in("student_number", allStudentNumber);
        }

        Integer roleCount = leaveNoteMapper.selectCount(wrapper1);
        Integer transmit = leaveNoteMapper.selectCount(wrapper2);
        Integer processed = leaveNoteMapper.selectCount(wrapper3);
        Integer waitReport = leaveNoteMapper.selectCount(wrapper4);
        Integer applyExpired = leaveNoteMapper.selectCount(wrapper5);
        Integer reportExpired = leaveNoteMapper.selectCount(wrapper6);
        Integer failure = leaveNoteMapper.selectCount(wrapper7);

       log.info(" roleCount{},transmit{},process{},waitReport{},applyExpired{},reportExpired{}",roleCount,transmit,processed,waitReport,applyExpired,reportExpired);

        Map<String,Integer> map  = new HashMap<>();
        map.put("PROCESSING",roleCount);
        map.put("TRANSMIT",transmit);
        map.put("PROCESSED",processed);
        map.put("WAIT_REPORT",waitReport);
        map.put("APPLY_EXPIRED",applyExpired);
        map.put("REPORT_EXPIRED",reportExpired);
        map.put("FAILURE",failure);


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
        if (!Objects.isNull(downloadNoteDTO.getStartTime())){
            wrapper.gt("start_time",downloadNoteDTO.getStartTime());
        }
        if (!Objects.isNull(downloadNoteDTO.getEndTime())){
            wrapper.lt("end_time",downloadNoteDTO.getEndTime());
        }
        if (!Objects.isNull(downloadNoteDTO.getStudentNumber())){
            wrapper.eq("student_number",downloadNoteDTO.getStudentNumber());
        }
        if (!Objects.isNull(downloadNoteDTO.getGradeId())){
            wrapper.eq("grade_Id",downloadNoteDTO.getGradeId());
        }
        if (!Objects.isNull(downloadNoteDTO.getClassId())){
            wrapper.eq("class_id",downloadNoteDTO.getClassId());
        }

        List<LeaveNote> leaveNotes = leaveNoteMapper.selectList(wrapper);
        return leaveNotes;
    }

    @Override
    public Map<String, Integer> allCountForGrade() {

        //待销假
        Integer waitReport = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.WAIT_REPORT)

        );
        //申请过期
        Integer applyExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.APPLY_EXPIRED)

        );
        //销假过期
        Integer reportExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.REPORT_EXPIRED)
        );

        //已拒绝
        Integer failure = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.FAILURE)
        );

        //审核中
        Integer processing = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().in("examine",
                        Arrays.asList(ExamineEnum.INSTRUCTOR.getValue(),
                                ExamineEnum.SECRETARY.getValue(),
                                ExamineEnum.DEAN.getValue()))
        );
        Integer processed = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.PROCESSED)

        );

        return getStringIntegerMap(waitReport, applyExpired, reportExpired, failure, processing,processed);
    }

    @Override
    public Map<String, Integer> allCountForGradeId(Long gradeId) {

        //待销假
        Integer waitReport = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.WAIT_REPORT)
                .eq("grade_id",gradeId)

        );
        //申请过期
        Integer applyExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.APPLY_EXPIRED)
                        .eq("grade_id",gradeId)
        );
        //销假过期
        Integer reportExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.REPORT_EXPIRED)
                        .eq("grade_id",gradeId)
        );

        //已拒绝
        Integer failure = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.FAILURE)
                        .eq("grade_id",gradeId)
        );

        //审核中
        Integer processing = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().in("examine",
                        Arrays.asList(ExamineEnum.INSTRUCTOR.getValue(),
                                ExamineEnum.SECRETARY.getValue(),
                                ExamineEnum.DEAN.getValue()))
                        .eq("grade_id",gradeId)
        );

        Integer processed = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.PROCESSED)
                        .eq("grade_id",gradeId)

        );


        return getStringIntegerMap(waitReport, applyExpired, reportExpired, failure, processing,processed);
    }

    private Map<String, Integer> getStringIntegerMap(Integer waitReport, Integer applyExpired, Integer reportExpired, Integer failure, Integer processing,Integer processed) {
        Map<String,Integer> map  = new HashMap<>();
        map.put("PROCESSING",processing);
        map.put("PROCESSED",processed);
        map.put("WAIT_REPORT",waitReport);
        map.put("APPLY_EXPIRED",applyExpired);
        map.put("REPORT_EXPIRED",reportExpired);
        map.put("FAILURE",failure);

        return map;
    }


}
