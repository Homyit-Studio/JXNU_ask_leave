package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 12:54
 */
public interface SysStudentClassMapper extends BaseMapper<SysStudentClass> {

    List<Long> selectAllStudentNumber(Long studentNumber);
}
