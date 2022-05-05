package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ClassInfoMapper extends BaseMapper<SysStudentClassInfo> {
    List<ClassInfoVO>  selectClassInfoByNumber(Long number);
}
