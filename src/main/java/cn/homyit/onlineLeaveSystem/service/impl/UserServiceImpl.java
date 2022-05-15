package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysClassStudent;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysUserRole;
import cn.homyit.onlineLeaveSystem.eneity.DTO.TeacherUpdaterDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.mapper.SysClassStudentMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentUserMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysUserRoleMapper;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.service.UserService;
import cn.homyit.onlineLeaveSystem.util.JwtUtil;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import cn.homyit.onlineLeaveSystem.util.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 州牧
 * @description
 * @since 2022-05-03 19:06
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysStudentUserMapper userMapper;

    @Autowired
    private SysClassStudentMapper sysClassStudentMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    //登录
    @Override
    public HashMap<String,String> login(SysStudentUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getStudentNumber(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);

        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("role",loginUser.getUser().getRole().toString());
        return map;

    }

    //退出登录
    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
    }

    /*获取个人信息*/
    @Override
    public StudentUserVo personInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        StudentUserVo userInfo = new StudentUserVo();
        BeanUtils.copyProperties(loginUser.getUser(),userInfo);
        return userInfo;
    }

    /*修改密码*/
    @Override
    public void updatePWD(PasswordDTO passwordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        boolean matches = passwordEncoder.matches(passwordDTO.getOldPassword(), loginUser.getPassword());

        if (!matches){
            throw new RuntimeException("密码输入错误");
        }

        SysStudentUser user = new SysStudentUser();
        user.setId(loginUser.getUser().getId());
        String encode = passwordEncoder.encode(passwordDTO.getNewPassword());
        user.setPassword(encode);
        userMapper.updateById(user);


    }

    //获取所有负责人
    @Override
    public Map<String, Long> getAllLeaders() {

        QueryWrapper<SysStudentUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role", LevelEnum.SECRETARY)
        .select("student_number","username");
        List<SysStudentUser> sysStudentUsers = userMapper.selectList(wrapper);

        Map<String, Long> map = new HashMap<>();
        for (SysStudentUser sysStudentUser : sysStudentUsers) {
            System.out.println(sysStudentUser);
            map.put(sysStudentUser.getUsername(),sysStudentUser.getStudentNumber());
        }
        return map;
    }

    //根据姓名获取学生列表
    @Override
    public List<StudentUserVo> getNoteByStudentName(String username) {
        QueryWrapper<SysStudentUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        List<SysStudentUser> sysStudentUsers = userMapper.selectList(wrapper);
        List<StudentUserVo> list = MyBeanUtils.copyList(sysStudentUsers, StudentUserVo.class);
        return list;
    }

    //增加下级
    @Override
    public void addUser(TeacherUpdaterDTO teacherUpdaterDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();
        if (role.getValue()<=teacherUpdaterDTO.getRole().getValue()){
            throw new RuntimeException("角色增加错误，请您重试");
        }


        SysStudentUser user = MyBeanUtils.copyBean(teacherUpdaterDTO, SysStudentUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //插入用户表
        userMapper.insert(user);
        //插入用户班级表
        sysClassStudentMapper.insert(new SysClassStudent(teacherUpdaterDTO.getClassId(), teacherUpdaterDTO.getStudentNumber()));
        //插入用户角色表
        sysUserRoleMapper.insert(new SysUserRole(teacherUpdaterDTO.getStudentNumber(), teacherUpdaterDTO.getRole().getValue().longValue()));

    }

    //更新学生信息
    @Override
    public void updateUser(TeacherUpdaterDTO teacherUpdaterDTO) {
        SysStudentUser user = MyBeanUtils.copyBean(teacherUpdaterDTO, SysStudentUser.class);

        SysStudentUser oldUser = userMapper.selectById(teacherUpdaterDTO.getId());
        //更新学号信息
        if (!Objects.isNull(teacherUpdaterDTO.getStudentNumber())){
            QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("student_number",oldUser.getStudentNumber());
            SysUserRole sysUserRole = new SysUserRole(teacherUpdaterDTO.getStudentNumber(), null);
            sysUserRoleMapper.update(sysUserRole,wrapper);

            QueryWrapper<SysClassStudent> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("student_number",oldUser.getStudentNumber());
            SysClassStudent sysClassStudent = new SysClassStudent(null, teacherUpdaterDTO.getStudentNumber());
            sysClassStudentMapper.update(sysClassStudent,wrapper1);


        }
        //更新班级信息
        if (!Objects.isNull(teacherUpdaterDTO.getClassId())){
            QueryWrapper<SysClassStudent> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("student_number", teacherUpdaterDTO.getStudentNumber());
            SysClassStudent sysClassStudent = new SysClassStudent(teacherUpdaterDTO.getClassId(), null);
            sysClassStudentMapper.update(sysClassStudent,wrapper1);
        }


        user.setPassword(null);
        userMapper.updateById(user);
    }

    //删除学生信息
    //todo 为什么联合主键不是逻辑删除
    @Override
    public void deletedUser(TeacherUpdaterDTO teacherUpdaterDTO) {
        userMapper.deleteById(teacherUpdaterDTO.getId());

        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("student_number", teacherUpdaterDTO.getStudentNumber());
        sysUserRoleMapper.delete(wrapper);

        QueryWrapper<SysClassStudent> wrapper1 = new QueryWrapper<>();
        wrapper.eq("student_number", teacherUpdaterDTO.getStudentNumber());
        sysClassStudentMapper.delete(wrapper1);
    }

    @Override
    public void updateUserForStudent(StudentUpdateDTO studentUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long studentNumber = loginUser.getUser().getStudentNumber();
//        SysStudentUser user = userMapper.selectOne(
//                new QueryWrapper<SysStudentUser>().
//                        eq("student_number", studentNumber));
        SysStudentUser user = MyBeanUtils.copyBean(studentUpdateDTO, SysStudentUser.class);
        userMapper.update(user,new QueryWrapper<SysStudentUser>().
                        eq("student_number", studentNumber));

    }


}
