package cn.homyit.onlineLeaveSystem.mapper;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu>{
    List<String> selectPermsByUserId(Long id);
}
