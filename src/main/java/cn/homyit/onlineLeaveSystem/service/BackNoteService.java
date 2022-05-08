package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.BackNoteVo;

public interface BackNoteService {

    void insertNote(BackNote backNote);

    BackNoteVo selectANote(Long id);

    void updateNote(BackNoteDTO backNoteVoDTO);
}
