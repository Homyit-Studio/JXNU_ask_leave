package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClass;
import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentClassInfo;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.eneity.VO.ClassStudentVo;
import cn.homyit.onlineLeaveSystem.eneity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.mapper.ClassInfoMapper;
import cn.homyit.onlineLeaveSystem.mapper.ClassStudentMapper;
import cn.homyit.onlineLeaveSystem.mapper.SysStudentClassMapper;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 14:54
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Autowired
    private ClassStudentMapper classStudentMapper;

    @Autowired
    private SysStudentClassMapper sysStudentClassMapper;

    @Override
    public List<ClassInfoVO> getClassInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long studentNumber = loginUser.getUser().getStudentNumber();

        return classInfoMapper.selectClassInfoByNumber(studentNumber);
    }

    @Override
    public PageVo<ClassStudentVo> getStudentsByClassId(Long classId) {
        Page<SysStudentClass> page = new Page<>(1,10);
        QueryWrapper<SysStudentClass> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id",classId);
        wrapper.orderByAsc("student_number");

        IPage<SysStudentClass> iPage = classStudentMapper.selectPage(page, wrapper);

        List<ClassStudentVo> list = null;
        try {
            list = MyBeanUtils.copyList(iPage.getRecords(),ClassStudentVo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("拷贝异常");
        }
        return new PageVo<>(list,iPage.getTotal(),iPage.getPages());

    }

    /*获取教师所属班级下所有学生的学号*/

    @Override
    public List<Long> getAllStudentNumber(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long studentNumber = loginUser.getUser().getStudentNumber();
        List<Long> list = sysStudentClassMapper.selectAllStudentNumber(studentNumber);
        return list;
    }


}
