package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.StudentExcelDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.TeacherExcelDTO;
import cn.homyit.onlineLeaveSystem.entity.VO.LeaveNoteVo;
import cn.homyit.onlineLeaveSystem.listener.EastExcelListener;
import cn.homyit.onlineLeaveSystem.listener.TeacherExcelListener;
import cn.homyit.onlineLeaveSystem.mapper.*;
import cn.homyit.onlineLeaveSystem.service.ExcelService;
import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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

    @Autowired
    private LeaveNoteService leaveNoteService;


    @Override
    public void upload(MultipartFile file,Long gradeId)  {
        ExcelReader reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream(), StudentExcelDTO.class,
                new EastExcelListener(userMapper,passwordEncoder,classInfoMapper,sysClassStudentMapper,sysUserRoleMapper,gradeId)).build();
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
            reader = EasyExcel.read(file.getInputStream(), TeacherExcelDTO.class,
                    new TeacherExcelListener(
                             userMapper,
                             passwordEncoder,
                             sysClassTeacherMapper,
                             classInfoMapper,
                            sysUserRoleMapper,
                            sysClassStudentMapper

                    )).build();
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("excel导入错误");
        }
        ReadSheet sheet = EasyExcel.readSheet(0).headRowNumber(2).build();
        reader.read(sheet);
        reader.finish();
    }

    @Override
    public void download(DownloadNoteDTO downloadNoteDTO, HttpServletResponse response)  {
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); //设置响应内容类型
        response.setCharacterEncoding("utf-8");//编码
        String fileName = null;//设置文件名
        try {
            fileName = URLEncoder.encode("请假条", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("文件名称错误");
        }
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");//设置响应头

        List<LeaveNoteVo> list1  = leaveNoteService.selectNoteToDownload(downloadNoteDTO);

//        List<LeaveNoteVo> list1 = MyBeanUtils.copyList(list, LeaveNoteVo.class);

        ExcelWriter writer = null;//获取写出流
        try {
            writer = EasyExcel.write(response.getOutputStream(),
                    LeaveNoteVo.class).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WriteSheet sheet = EasyExcel.writerSheet("one").build();//创建表格，设置表格页名称
        writer.write(list1,sheet);//读出
        writer.finish();//关闭流
    }


}
