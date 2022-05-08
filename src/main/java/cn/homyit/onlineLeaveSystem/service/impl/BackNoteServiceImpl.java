package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.BackNote;
import cn.homyit.onlineLeaveSystem.eneity.DTO.BackNoteDTO;
import cn.homyit.onlineLeaveSystem.service.BackNoteService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 22:44
 */
@Service
public class BackNoteServiceImpl implements BackNoteService {

    @Override
    public void create(BackNoteDTO backNoteDTO) {
        BackNote backNote = MyBeanUtils.copyBean(backNoteDTO, BackNote.class);
        
    }
}
