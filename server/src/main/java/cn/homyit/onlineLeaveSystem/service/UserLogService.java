package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DTO.UserLogDTO;

public interface UserLogService {
    /**
     * 插入用户操作日志
     *
     * @param userLogDTO
     * @return
     */
    void addSysLog(UserLogDTO userLogDTO);
}
