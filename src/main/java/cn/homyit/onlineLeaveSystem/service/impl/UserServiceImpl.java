package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.PasswordDTO;
import cn.homyit.onlineLeaveSystem.eneity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentUserMapper;
import cn.homyit.onlineLeaveSystem.service.UserService;
import cn.homyit.onlineLeaveSystem.util.JwtUtil;
import cn.homyit.onlineLeaveSystem.util.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

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
    PasswordEncoder passwordEncoder;

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
        return map;

    }

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


}
