package cn.homyit.onlineLeaveSystem.listener;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysClassStudent;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysUserRole;
import cn.homyit.onlineLeaveSystem.eneity.DTO.StudentDTO;
import cn.homyit.onlineLeaveSystem.mapper.ClassInfoMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysClassStudentMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentUserMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysUserRoleMapper;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

/**
 * @author 州牧
 * @description 使用hashset去重创建班级和班级对应学生
 * @since 2022-05-05 20:26
 */
public class EastExcelListener extends AnalysisEventListener<StudentDTO> {


    private PasswordEncoder passwordEncoder;
    private SysStudentUserMapper userMapper;
    private ClassInfoMapper classInfoMapper;
    private SysClassStudentMapper sysClassStudentMapper;
    private SysUserRoleMapper sysUserRoleMapper;
    private Map<String, LevelEnum> roleMap;

    private HashSet<String> classSet ;

    public EastExcelListener(SysStudentUserMapper userMapper,
                             PasswordEncoder passwordEncoder,
                             ClassInfoMapper classInfoMapper,
                             SysClassStudentMapper sysClassStudentMapper,
                             SysUserRoleMapper sysUserRoleMapper){
        this.userMapper =userMapper;
        this.passwordEncoder = passwordEncoder;
        this.classInfoMapper = classInfoMapper;
        this.sysClassStudentMapper = sysClassStudentMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        roleMap = new HashMap<>();
        roleMap.put("辅导员",LevelEnum.INSTRUCTOR);
        roleMap.put("党委副书记",LevelEnum.SECRETARY);
        roleMap.put("院长",LevelEnum.DEAN);
        roleMap.put("学生",LevelEnum.STUDENT);
        roleMap.put("无",LevelEnum.LOOK);

        classSet = new HashSet<>();
    }
    //todo 开启事务

    @Override
    public void invoke(StudentDTO data, AnalysisContext context) {

        //插入班级表
        SysStudentClassInfo sysStudentClassInfo = new SysStudentClassInfo();
        String majorAndClass = data.getMajorAndClass().trim();
        if(!classSet.contains(majorAndClass)){
            classSet.add(majorAndClass);
            sysStudentClassInfo.setMajorAndClass(data.getMajorAndClass());
            classInfoMapper.insert(sysStudentClassInfo);
        }

        SysStudentUser user = MyBeanUtils.copyBean(data, SysStudentUser.class);

        if (data.equals("男")){
            user.setSex(SexEnum.MAN);
        }else{
            user.setSex(SexEnum.WOMAN);
        }
        String rawPWD = data.getStudentNumber().toString().substring(6, 12);
        user.setPassword(passwordEncoder.encode(rawPWD));



        //插入用户班级表
        SysClassStudent sysClassStudent = new SysClassStudent();
        sysClassStudent.setStudentNumber(data.getStudentNumber());
                //获得班级id
        QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("major_and_class",data.getMajorAndClass())
                .select("id");
        Long classId = classInfoMapper.selectOne(wrapper).getId();

        //插入用户表
        user.setClassId(classId);
        userMapper.insert(user);
            //插入用户班级表
        sysClassStudentMapper.insert(new SysClassStudent(classId, data.getStudentNumber()));

        //插入用户角色表
        sysUserRoleMapper.insert(new SysUserRole(data.getStudentNumber(), LevelEnum.STUDENT.getValue().longValue()));

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //完善容量

        QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        List<SysStudentClassInfo> ids = classInfoMapper.selectList(wrapper);
        for (SysStudentClassInfo info : ids) {
            Long class_id = info.getId();
            QueryWrapper<SysClassStudent> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("class_id",class_id);
            Integer count = sysClassStudentMapper.selectCount(wrapper1);
            info.setCapacity(count);
            System.out.println("======================");
            System.out.println("class_id"+class_id+"  count"+count);
            System.out.println("======================");
            classInfoMapper.updateById(info);
        }

    }

}
