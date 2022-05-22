package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.SelectNotePageDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.TableTimeDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UpdateNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;

import java.util.List;
import java.util.Map;


public interface LeaveNoteService {
    void insertNote(LeaveNote note);

    PageVo<LeaveNoteVo> selectLeaveNote(SelectNotePageDTO selectNoteDTO);

    PageVo<LeaveNoteVo> selectNoteByRole(SelectNotePageDTO selectNoteDTO);

    /*选出一个假条进行审核*/

    LeaveNoteVo selectANote(Long studentNumber);

    void updateNote(UpdateNoteDTO updateNoteDTO);

    PageVo<LeaveNoteVo> selectNodeByGrade(SelectNotePageDTO selectNoteDTO);

    void deletedANote(Long id);

    Map<String,Integer>  allCounts();


    void checkExpired();

    List<LeaveNoteVo> selectNoteToDownload(DownloadNoteDTO downloadNoteDTO);

    Map<String, Integer> allCountForGrade(TableTimeDTO tableTimeDTO);

    Map<String, Integer> allCountForGradeId(TableTimeDTO tableTimeDTO);


//    List<ImagesNote> selectImageForNote(Long noteId);
}
