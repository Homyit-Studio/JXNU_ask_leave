package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.SysDiscussion;
import cn.homyit.onlineLeaveSystem.entity.DTO.DiscussionDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PageDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;

public interface DiscussionService {
    Long add(SysDiscussion discussion);

    PageVo<SysDiscussion> selectPage(DiscussionDTO discussionDTO);

    void delete(Long discussionId);
}
