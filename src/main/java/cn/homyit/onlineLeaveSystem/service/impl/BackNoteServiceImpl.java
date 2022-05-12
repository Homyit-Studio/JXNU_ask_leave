package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DO.LeaveNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.BackNoteVo;
import cn.homyit.onlineLeaveSystem.mapper.BackNoteMapper;
import cn.homyit.onlineLeaveSystem.mapper.LeaveNoteMapper;
import cn.homyit.onlineLeaveSystem.myEnum.BackEnum;
import cn.homyit.onlineLeaveSystem.myEnum.BackStatusEnum;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 22:44
 */
@Service
public class BackNoteServiceImpl implements BackNoteService {

  @Autowired
  private LeaveNoteMapper leaveNoteMapper;

  @Autowired
  private BackNoteMapper backNoteMapper;

    @Override
    public void insertNote(BackNote backNote) {
        backNoteMapper.insert(backNote);
    }

  @Override
  public BackNoteVo selectANote(Long id) {
    BackNote backNote = backNoteMapper.selectById(id);
    BackNoteVo backNoteVo = MyBeanUtils.copyBean(backNote, BackNoteVo.class);
    return backNoteVo;
  }

  @Override
  public void updateNote(BackNoteDTO backNoteVoDTO) {
    BackNote backNote = MyBeanUtils.copyBean(backNoteVoDTO, BackNote.class);
    if(backNoteVoDTO.getBack().equals(BackEnum.YES)){
      backNote.setStatus(BackStatusEnum.SUCCESS);
      LeaveNote note = new LeaveNote();
      note.setId(backNote.getId());
      note.setStatus(BackStatusEnum.SUCCESS);
      leaveNoteMapper.updateById(note);
    }else{
      backNote.setStatus(BackStatusEnum.FAILURE);
    }
    System.out.println(backNote);
    backNoteMapper.updateById(backNote);
  }


}
