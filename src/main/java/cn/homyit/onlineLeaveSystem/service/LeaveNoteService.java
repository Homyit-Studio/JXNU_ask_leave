package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.SimpleNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;


public interface LeaveNoteService {
    void insertNote(LeaveNote note);

    PageVo<SimpleNote> selectSimpleNote(SelectNotePageDTO selectNoteDTO);

    PageVo<SimpleNote> selectNoteByRole(SelectNotePageDTO selectNoteDTO);

    /*选出一个假条进行审核*/

    LeaveNoteVo selectANote(Long studentNumber);

    void updateNote(UpdateNoteDTO updateNoteDTO);

}
