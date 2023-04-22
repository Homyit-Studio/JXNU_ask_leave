package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.entity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.BackNoteVo;

public interface BackNoteService {

    void insertNote(BackNote backNote);

    BackNoteVo selectANote(Long id);

    void updateNote(BackNoteDTO backNoteVoDTO);
}
