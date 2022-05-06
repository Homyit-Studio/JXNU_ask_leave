package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysStudentUserMapper extends BaseMapper<SysStudentUser> {
    List<Long> selectAllStudentNumber(Long studentNumber);
}
