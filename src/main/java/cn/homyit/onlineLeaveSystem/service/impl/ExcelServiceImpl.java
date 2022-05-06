package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.eneity.DTO.StudentDTO;
import cn.homyit.onlineLeaveSystem.eneity.DTO.TeacherDTO;
import cn.homyit.onlineLeaveSystem.listener.EastExcelListener;
import cn.homyit.onlineLeaveSystem.listener.TeacherExcelListener;
import cn.homyit.onlineLeaveSystem.mapper.*;
import cn.homyit.onlineLeaveSystem.service.ExcelService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 20:05
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private SysStudentUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Autowired
    private SysClassStudentMapper sysClassStudentMapper;

    @Autowired
    private SysClassTeacherMapper sysClassTeacherMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public void upload(MultipartFile file)  {
        ExcelReader reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream(), StudentDTO.class,
                new EastExcelListener(userMapper,passwordEncoder,classInfoMapper,sysClassStudentMapper,sysUserRoleMapper)).build();
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("excel导入错误");
        }
        ReadSheet sheet = EasyExcel.readSheet(0).headRowNumber(2).build();
        reader.read(sheet);
        reader.finish();

    }

    @Override
    public void uploadTeacher(MultipartFile file) {
        ExcelReader reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream(), TeacherDTO.class,
                    new TeacherExcelListener(
                             userMapper,
                             passwordEncoder,
                             sysClassTeacherMapper,
                             classInfoMapper,
                             sysUserRoleMapper
                    )).build();
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("excel导入错误");
        }
        ReadSheet sheet = EasyExcel.readSheet(0).headRowNumber(2).build();
        reader.read(sheet);
        reader.finish();
    }


}
