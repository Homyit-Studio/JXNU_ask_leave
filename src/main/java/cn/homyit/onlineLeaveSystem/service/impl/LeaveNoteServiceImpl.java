package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.*;
import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.TableTimeDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.myEnum.*;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import cn.homyit.onlineLeaveSystem.service.ImageService;
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

@Slf4j
@Service
public class LeaveNoteServiceImpl implements LeaveNoteService {

    @Autowired
    private LeaveNoteMapper leaveNoteMapper;

    @Autowired
    private ImageService imageService;

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
        note.setGradeId(user.getGradeId());

        Integer days = Math.toIntExact(diff / (24 * 60 * 60 * 1000));
        if (days<=3){
            note.setLevel(LevelEnum.INSTRUCTOR);
        }else if(days>3&&days<=7){
            note.setLevel(LevelEnum.SECRETARY);
        }else {
            note.setLevel(LevelEnum.DEAN);
        }
        leaveNoteMapper.insert(note);



    }

    @Override
    public PageVo<LeaveNoteVo> selectNodeByGrade(SelectNotePageDTO selectNoteDTO) {

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
        List<LeaveNoteVo> list = MyBeanUtils.copyList(iPage.getRecords(), LeaveNoteVo.class);
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());
    }

    @Override
    public void deletedANote(Long id) {

        LeaveNote note = leaveNoteMapper.selectById(id);
        if (note.getExamine().getValue()>=ExamineEnum.WAIT_REPORT.getValue()){
            throw new BizException(ExceptionCodeEnum.DELETED_NOTE);
        }
        //删除假条，未生成销假条之前
        leaveNoteMapper.deleteById(id);
        imageService.deleteByNoteId(id);
    }



    //根据学号获取假条
    @Override
    public PageVo<LeaveNoteVo> selectLeaveNote(SelectNotePageDTO selectNoteDTO) {
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

        IPage<LeaveNote> iPage =leaveNoteMapper.selectPage(page,wrapper);
        List<LeaveNote> list  = null;
        try {
            list = MyBeanUtils.deepCopy(iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("深拷贝异常");
        }
        List<LeaveNoteVo> list1 = MyBeanUtils.copyList(list, LeaveNoteVo.class);
        return new PageVo<>(list1,iPage.getTotal(),iPage.getPages());

    }


    @Override
    public PageVo<LeaveNoteVo> selectNoteByRole(SelectNotePageDTO selectNoteDTO) {

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
                wrapper.apply("examine<level and examine!=-1");
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
        List<LeaveNoteVo> list = MyBeanUtils.copyList(iPage.getRecords(), LeaveNoteVo.class);
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());
    }

    @Override
    public LeaveNoteVo selectANote(Long id) {
        LeaveNote note = leaveNoteMapper.selectById(id);
        LeaveNoteVo leaveNoteVo = MyBeanUtils.copyBean(note, LeaveNoteVo.class);
        return leaveNoteVo;
    }

    //审批
    @Override
    public void updateNote(UpdateNoteDTO updateNoteDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();

        LeaveNote note = MyBeanUtils.copyBean(updateNoteDTO, LeaveNote.class);
        //角色级别小于审核级别
        if (role.getValue()<=updateNoteDTO.getLevelEnum().getValue()){
            throw new BizException(ExceptionCodeEnum.ALREADY_AGREE);
        }

        if (updateNoteDTO.getOpinionEnum().equals(OpinionEnum.NO)){
            note.setExamine(ExamineEnum.FAILURE);
        }else{
            //院长审批或最后一级审批生成销假条或者跨级
            if(role.equals(LevelEnum.DEAN)||updateNoteDTO.getLevelEnum().getValue()<=role.getValue()){
                note.setExamine(ExamineEnum.WAIT_REPORT);

                BackNote backNote = MyBeanUtils.copyBean(note, BackNote.class);
                backNote.setDepart(LeaveEnum.NO);
                backNoteService.insertNote(backNote);
            }else{
                note.setExamine(ExamineEnum.getEumByCode(role.getValue()));
                if (role.equals(LevelEnum.INSTRUCTOR)){
                    //手动指定负责人
                    note.setLeaderNumber(updateNoteDTO.getLeaderNumber());
                }
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
        QueryWrapper<LeaveNote> wrapper1 = new QueryWrapper<>();

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

            wrapper1.in("student_number", allStudentNumber)
                    .eq("examine", ExamineEnum.getEumByCode(role.getValue().intValue() - 1));
            wrapper2.in("student_number", allStudentNumber);
            wrapper3.in("student_number", allStudentNumber);
            wrapper4.in("student_number", allStudentNumber);
            wrapper5.in("student_number", allStudentNumber);
            wrapper6.in("student_number", allStudentNumber);
            wrapper7.in("student_number", allStudentNumber);
        }else if (role.equals(LevelEnum.SECRETARY)) {
            wrapper1.like("leader_number", loginUser.getUser().getStudentNumber())
                    .eq("examine", ExamineEnum.getEumByCode(role.getValue().intValue() - 1));
            wrapper2.like("leader_number", loginUser.getUser().getStudentNumber());
            wrapper3.like("leader_number", loginUser.getUser().getStudentNumber());
            wrapper4.like("leader_number", loginUser.getUser().getStudentNumber());
            wrapper5.like("leader_number", loginUser.getUser().getStudentNumber());
            wrapper6.like("leader_number", loginUser.getUser().getStudentNumber());
            wrapper7.like("leader_number", loginUser.getUser().getStudentNumber());
        } else if(role.equals(LevelEnum.STUDENT)){
            Long studentNumber = loginUser.getUser().getStudentNumber();
            wrapper1.eq("student_number", studentNumber)
                    .apply("examine<level and examine!=-1");
            wrapper2.eq("student_number", studentNumber);
            wrapper3.eq("student_number", studentNumber);
            wrapper4.eq("student_number", studentNumber);
            wrapper5.eq("student_number", studentNumber);
            wrapper6.eq("student_number", studentNumber);
            wrapper7.eq("student_number", studentNumber);
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
    public List<LeaveNoteVo> selectNoteToDownload(DownloadNoteDTO downloadNoteDTO) {
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
        List<LeaveNoteVo> list = MyBeanUtils.copyList(leaveNotes, LeaveNoteVo.class);
        return list;
    }

    @Override
    public Map<String, Integer> allCountForGrade(TableTimeDTO tableTimeDTO) {

        //待销假
        Integer waitReport = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.WAIT_REPORT)
                .gt("start_time",tableTimeDTO.getStartTime())
                .lt("end_time",tableTimeDTO.getEndTime())

        );
        //申请过期
        Integer applyExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.APPLY_EXPIRED)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())

        );
        //销假过期
        Integer reportExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.REPORT_EXPIRED)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        //已拒绝
        Integer failure = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.FAILURE)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        //审核中
        Integer processing = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().in("examine",
                        Arrays.asList(ExamineEnum.INSTRUCTOR.getValue(),
                                ExamineEnum.SECRETARY.getValue(),
                                ExamineEnum.DEAN.getValue()))
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );
        Integer processed = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.PROCESSED)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        return getStringIntegerMap(waitReport, applyExpired, reportExpired, failure, processing,processed);
    }

    @Override
    public Map<String, Integer> allCountForGradeId(TableTimeDTO tableTimeDTO) {
        Long gradeId = tableTimeDTO.getGradeId();

        //待销假
        Integer waitReport = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.WAIT_REPORT)
                .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())

        );
        //申请过期
        Integer applyExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.APPLY_EXPIRED)
                        .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );
        //销假过期
        Integer reportExpired = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.REPORT_EXPIRED)
                        .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        //已拒绝
        Integer failure = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.FAILURE)
                        .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        //审核中
        Integer processing = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().in("examine",
                        Arrays.asList(ExamineEnum.INSTRUCTOR.getValue(),
                                ExamineEnum.SECRETARY.getValue(),
                                ExamineEnum.DEAN.getValue()))
                        .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())
        );

        Integer processed = leaveNoteMapper.selectCount(
                new QueryWrapper<LeaveNote>().eq("examine",ExamineEnum.PROCESSED)
                        .eq("grade_id",gradeId)
                        .gt("start_time",tableTimeDTO.getStartTime())
                        .lt("end_time",tableTimeDTO.getEndTime())

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
