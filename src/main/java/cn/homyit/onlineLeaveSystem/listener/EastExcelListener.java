package cn.homyit.onlineLeaveSystem.listener;

import cn.homyit.onlineLeaveSystem.entity.DO.SysClassStudent;
import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DO.SysUserRole;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentExcelDTO;
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
public class EastExcelListener extends AnalysisEventListener<StudentExcelDTO> {


    private PasswordEncoder passwordEncoder;
    private SysStudentUserMapper userMapper;
    private ClassInfoMapper classInfoMapper;
    private SysClassStudentMapper sysClassStudentMapper;
    private SysUserRoleMapper sysUserRoleMapper;
    private Map<String, LevelEnum> roleMap;

    private HashSet<String> classSet ;

    private  long gradeId;
    private  final long FOREIGN_GRADE_ID = 1;
    private  final long UNDERGRADUATE_GRADE_ID = 2;
    private  final long POSTGRADUATE_GRADE_ID = 3;





    public EastExcelListener(SysStudentUserMapper userMapper,
                             PasswordEncoder passwordEncoder,
                             ClassInfoMapper classInfoMapper,
                             SysClassStudentMapper sysClassStudentMapper,
                             SysUserRoleMapper sysUserRoleMapper,
                             Long gradeId){
        this.userMapper =userMapper;
        this.passwordEncoder = passwordEncoder;
        this.classInfoMapper = classInfoMapper;
        this.sysClassStudentMapper = sysClassStudentMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.gradeId =gradeId;
        roleMap = new HashMap<>();
        roleMap.put("辅导员",LevelEnum.INSTRUCTOR);
        roleMap.put("党委副书记",LevelEnum.SECRETARY);
        roleMap.put("院长",LevelEnum.DEAN);
        roleMap.put("学生",LevelEnum.STUDENT);
        roleMap.put("无",LevelEnum.LOOK);

        classSet = new HashSet<>();
    }

    @Override
    public void invoke(StudentExcelDTO data, AnalysisContext context) {


        String majorAndClass;
        //本科生
        if (gradeId/1000==UNDERGRADUATE_GRADE_ID){
            majorAndClass = data.getMajorAndClass().trim();
        }else {
            //研究生与留学生
            String s = data.getMajorAndClass();
            int index = s.indexOf("级")+1;
            majorAndClass = s.substring(0,index);
        }

        if(!classSet.contains(majorAndClass)){
            SysStudentClassInfo sysStudentClassInfo = new SysStudentClassInfo();
            classSet.add(majorAndClass);
            sysStudentClassInfo.setMajorAndClass(majorAndClass);
            sysStudentClassInfo.setGradeId(gradeId);
            //创建班级
            classInfoMapper.insert(sysStudentClassInfo);
        }

        SysStudentUser user = MyBeanUtils.copyBean(data, SysStudentUser.class);
        if (data.getSex().trim().equals("男")){
            user.setSex(SexEnum.MAN);
        }else{
            user.setSex(SexEnum.WOMAN);
        }
        String rawPWD = data.getPhoneNumber().trim();
        user.setPassword(passwordEncoder.encode(rawPWD));

        //获取班级id
        QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("major_and_class",majorAndClass)
                .eq("grade_id",gradeId)
                .select("id");
        Long classId = classInfoMapper.selectOne(wrapper).getId();
        user.setClassId(classId);
        user.setGradeId(gradeId);

        //插入用户表
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
