package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.entity.DO.SysClassStudent;
import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DO.SysUserRole;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherAddDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherUpdaterDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentUpdateDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
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
import org.springframework.util.CollectionUtils;

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
//        if(Objects.isNull(authenticate)){
//            throw new BizException(ExceptionCodeEnum.LOGIN_ERROR);
//        }自动检验了密码和抛出异常，todo 怎么抓service的异常呢，controller怎么抓到异常的

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
            throw new BizException(ExceptionCodeEnum.UPDATE_PWD_ERROR);
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
        if (CollectionUtils.isEmpty(list)){
            throw new BizException(ExceptionCodeEnum.NO_USER);
        }
        return list;
    }



    //增加下级
    @Override
    public void addUser(TeacherAddDTO teacherAddDTODTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LevelEnum role = loginUser.getUser().getRole();
        if (role.getValue()<=teacherAddDTODTO.getRole().getValue()){
            throw new BizException(ExceptionCodeEnum.ADD_USER);
        }
        //先查账号是否存在
        SysStudentUser checkExist = userMapper.selectById(teacherAddDTODTO.getStudentNumber());
        if (!Objects.isNull(checkExist)){
            throw new BizException(ExceptionCodeEnum.USER_EXIST);
        }

        SysStudentUser user = MyBeanUtils.copyBean(teacherAddDTODTO, SysStudentUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //插入用户表
        userMapper.insert(user);
        //插入用户班级表
        sysClassStudentMapper.insert(new SysClassStudent(teacherAddDTODTO.getClassId(), teacherAddDTODTO.getStudentNumber()));
        //插入用户角色表
        sysUserRoleMapper.insert(new SysUserRole(teacherAddDTODTO.getStudentNumber(), teacherAddDTODTO.getRole().getValue().longValue()));

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
        wrapper1.eq("student_number", teacherUpdaterDTO.getStudentNumber());
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
