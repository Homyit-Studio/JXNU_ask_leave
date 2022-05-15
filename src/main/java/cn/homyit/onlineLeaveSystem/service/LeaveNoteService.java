package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.ImagesNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;

import java.util.List;
import java.util.Map;


public interface LeaveNoteService {
    void insertNote(LeaveNote note);

    PageVo<LeaveNote> selectLeaveNote(SelectNotePageDTO selectNoteDTO);

    PageVo<LeaveNote> selectNoteByRole(SelectNotePageDTO selectNoteDTO);

    /*选出一个假条进行审核*/

    LeaveNoteVo selectANote(Long studentNumber);

    void updateNote(UpdateNoteDTO updateNoteDTO);

    PageVo<LeaveNote> selectNodeByGrade(SelectNotePageDTO selectNoteDTO);

    void deletedANote(Long id);

    Map<String,Integer>  allCounts();


    void checkExpired();

    List<LeaveNote> selectNoteToDownload(DownloadNoteDTO downloadNoteDTO);

    Map<String, Integer> allCountForGrade();

    Map<String, Integer> allCountForGradeId(Long gradeId);


//    List<ImagesNote> selectImageForNote(Long noteId);
}
