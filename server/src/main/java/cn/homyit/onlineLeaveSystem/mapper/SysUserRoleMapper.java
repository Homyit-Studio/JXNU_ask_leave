package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.entity.DO.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    void deleteForGrade(Long gradeId);
}
