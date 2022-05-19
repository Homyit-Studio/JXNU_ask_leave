package cn.homyit.onlineLeaveSystem.listener;

import cn.homyit.onlineLeaveSystem.entity.DO.*;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherDTO;
import cn.homyit.onlineLeaveSystem.mapper.*;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 州牧
 * @description
 * @since 2022-05-06 17:11
 */
public class TeacherExcelListener extends AnalysisEventListener<TeacherDTO> {

    private SysClassTeacherMapper sysClassTeacherMapper;
    private PasswordEncoder passwordEncoder;
    private SysStudentUserMapper userMapper;
    private ClassInfoMapper classInfoMapper;
    private SysUserRoleMapper sysUserRoleMapper;
    private Map<String,LevelEnum> roleMap;
    private SysClassStudentMapper sysClassStudentMapper;
    private static final Long CLASS_ID = 1L;




    public TeacherExcelListener(SysStudentUserMapper userMapper,
                             PasswordEncoder passwordEncoder,
                                SysClassTeacherMapper sysClassTeacherMapper,

                                ClassInfoMapper classInfoMapper,
                                SysUserRoleMapper sysUserRoleMapper,
            SysClassStudentMapper sysClassStudentMapper){
        this.userMapper =userMapper;
        this.passwordEncoder = passwordEncoder;
        this.sysClassTeacherMapper = sysClassTeacherMapper;
        this.sysClassStudentMapper = sysClassStudentMapper;
        this.classInfoMapper = classInfoMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        roleMap = new HashMap<>();
//        roleMap.put("辅导员",LevelEnum.INSTRUCTOR);
        roleMap.put("班主任",LevelEnum.INSTRUCTOR);
        roleMap.put("负责人",LevelEnum.SECRETARY);
        roleMap.put("院长",LevelEnum.DEAN);
        roleMap.put("学生",LevelEnum.STUDENT);
        roleMap.put("无",LevelEnum.LOOK);

    }




    @Override
    public void invoke(TeacherDTO data, AnalysisContext context) {
        SysStudentUser user = MyBeanUtils.copyBean(data, SysStudentUser.class);

        user.setGradeId(1L);
        if (data.getSex().equals("男")){
            user.setSex(SexEnum.MAN);
        }else{
            user.setSex(SexEnum.WOMAN);
        }
        String rawPWD = data.getPhoneNumber();
        user.setPassword(passwordEncoder.encode(rawPWD));
        //插入用户表
        LevelEnum role = roleMap.get(data.getRole().trim());



        //插入用户班级表
        String[] allClass = data.getManageClass().split("-");
        for (String aClass :  allClass) {
            //查找班级id
            QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("major_and_class",aClass)
                    .select("id");
            SysStudentClassInfo sysStudentClassInfo = classInfoMapper.selectOne(wrapper);

            if (!Objects.isNull(sysStudentClassInfo)){
                sysClassTeacherMapper.insert(new SysClassTeacher(sysStudentClassInfo.getId(), data.getStudentNumber()));
            }


        }

        //插入用户角色表
        if(Objects.isNull(role)){
            user.setRole(LevelEnum.getEumByCode(roleMap.get("无").getValue()));
            userMapper.insert(user);
            sysUserRoleMapper.insert(new SysUserRole(data.getStudentNumber(),roleMap.get("无").getValue().longValue()));
        }else if(LevelEnum.INSTRUCTOR.equals(role)){
          user.setClassId(CLASS_ID);
          new SysClassStudent(CLASS_ID, data.getStudentNumber());


        } else{
            user.setRole(LevelEnum.getEumByCode(role.getValue()));
            userMapper.insert(user);
            sysUserRoleMapper.insert(new SysUserRole(data.getStudentNumber(),role.getValue().longValue() ));
        }




    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //更新班主任容量
        Integer count = sysClassStudentMapper.selectCount(
                new QueryWrapper<SysClassStudent>().eq("class_id",CLASS_ID));
        SysStudentClassInfo sysStudentClassInfo = new SysStudentClassInfo();
        sysStudentClassInfo.setId(CLASS_ID);
        sysStudentClassInfo.setCapacity(count);
        classInfoMapper.updateById(sysStudentClassInfo);
    }
}
