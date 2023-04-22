package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.UserLogDO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserLogDTO;
import cn.homyit.onlineLeaveSystem.mapper.UserLogMapper;
import cn.homyit.onlineLeaveSystem.service.UserLogService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 州牧
 * @description
 * @since 2022-05-22 16:16
 */
@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public void addSysLog(UserLogDTO userLogDTO) {

        UserLogDO userLogDO = MyBeanUtils.copyBean(userLogDTO, UserLogDO.class);

        userLogMapper.insert(userLogDO);
    }

}
