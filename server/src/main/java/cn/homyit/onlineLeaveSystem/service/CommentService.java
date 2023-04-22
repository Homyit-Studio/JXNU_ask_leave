package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DO.SysComment;
import cn.homyit.onlineLeaveSystem.entity.DTO.CommentPageDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.GetSonDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.CommentVO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 17:10
 */
public interface CommentService {
    void add(SysComment comment);

    void delete(Long commentId);

    PageVo<CommentVO> get(CommentPageDTO commentDTO);

    PageVo<SysComment> getSon(GetSonDTO getSonDTO);

}
