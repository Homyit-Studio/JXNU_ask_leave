package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.*;
import cn.homyit.onlineLeaveSystem.entity.DTO.PageStudentDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.ClassInfoVO;
import cn.homyit.onlineLeaveSystem.entity.VO.PageVo;
import cn.homyit.onlineLeaveSystem.entity.VO.StudentUserVo;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.mapper.*;
import cn.homyit.onlineLeaveSystem.service.TeacherService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


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
    private SysStudentUserMapper studentUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysClassStudentMapper classStudentMapper;

    @Autowired
    private SysClassTeacherMapper classTeacherMapper;

    //获取管理班级
    @Override
    public List<ClassInfoVO> getClassInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long studentNumber = loginUser.getUser().getStudentNumber();
        List<ClassInfoVO> classInfoVOS = classInfoMapper.selectClassInfoByNumber(studentNumber);
        if (CollectionUtils.isEmpty(classInfoVOS)){
            throw new BizException(ExceptionCodeEnum.NO_CLASS);
        }
        return classInfoVOS;
    }

    //获取一个班级
    @Override
    public  PageVo<StudentUserVo>  getStudentsByClassId(PageStudentDTO pageStudentDTO) {
        Page<SysStudentUser> page = new Page<>(pageStudentDTO.getPageNo(), pageStudentDTO.getPageSize());
        QueryWrapper<SysStudentUser> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", pageStudentDTO.getClassId());
        wrapper.orderByAsc("student_number");
        IPage<SysStudentUser> iPage = studentUserMapper.selectPage(page, wrapper);

        List<StudentUserVo> list = null;
        try {
            list = MyBeanUtils.copyList(iPage.getRecords(),StudentUserVo.class);
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

        List<Long> list  = studentUserMapper.selectAllStudentNumber(studentNumber);
        return list;
    }

    //获取所有班级
    @Override
    public List<ClassInfoVO> getAllClass(Long gradeId) {
        QueryWrapper<SysStudentClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("grade_id",gradeId);
        wrapper.orderByAsc("id");

        List<SysStudentClassInfo> sysStudentClassInfos = classInfoMapper.selectList(wrapper);
        List<ClassInfoVO> classInfoVOS = MyBeanUtils.copyList(sysStudentClassInfos, ClassInfoVO.class);

        return classInfoVOS;

    }

    @Override
    public void deleteForGrade(Long gradeId) {
        sysUserRoleMapper.deleteForGrade(gradeId);

        classStudentMapper.deleteForGrade(gradeId);
        //教师管理班级同样也删除


        studentUserMapper.delete(new QueryWrapper<SysStudentUser>().
                eq("grade_id",gradeId));

        classInfoMapper.delete(new QueryWrapper<SysStudentClassInfo>().
                eq("grade_id",gradeId));


    }

//    @Override
//    public HashMap<Integer, Integer> getAllGradeId() {
//        return classInfoMapper.getAllGradeId();
//    }


}
