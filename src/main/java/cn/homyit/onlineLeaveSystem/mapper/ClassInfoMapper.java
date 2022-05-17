package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.entity.VO.ClassInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ClassInfoMapper extends BaseMapper<SysStudentClassInfo> {
    List<ClassInfoVO>  selectClassInfoByNumber(Long number);
}
