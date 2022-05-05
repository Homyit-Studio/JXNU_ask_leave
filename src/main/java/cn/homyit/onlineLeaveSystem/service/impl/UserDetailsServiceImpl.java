package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.mapper.SysMenuMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysStudentUserMapper studentUserMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<SysStudentUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudentUser::getStudentNumber,username);
        SysStudentUser user = studentUserMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("账号输入错误");
        }

        //封装成UserDetails对象返回 
        List<String> list =  menuMapper.selectPermsByUserId(user.getStudentNumber());
        System.out.println("============perms================");
        System.out.println(list);
        return new LoginUser(user,list);
    }
}