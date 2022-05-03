package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.SimpleNote;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.mapper.SimpleNoteMapper;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 州牧
 * @description 添加事务
 * @since 2022-05-03 21:31
 */
@Service
public class LeaveNoteServiceImpl implements LeaveNoteService {

    @Autowired
    private LeaveNoteMapper leaveNoteMapper;
    @Autowired
    private SimpleNoteMapper simpleNoteMapper;

    /*生成请假条*/
    @Override
    public void insertNote(LeaveNote note) {
        leaveNoteMapper.insert(note);
        SimpleNote simpleNote = new SimpleNote();
        BeanUtils.copyProperties(note,simpleNote);
        long diff = note.getEndTime().getTime()-note.getStartTime().getTime();
        String diffStr = ""+diff/ (24 * 60 * 60 * 1000)+"天"+ diff/( 60 * 60 * 1000)%24+"时";
        simpleNote.setDays(diffStr);
        simpleNoteMapper.insert(simpleNote);
    }
}
