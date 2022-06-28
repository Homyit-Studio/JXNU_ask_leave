package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTOForGet;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.log.UserLog;
import cn.homyit.onlineLeaveSystem.myEnum.ModuleEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OperationEnum;
import cn.homyit.onlineLeaveSystem.service.ExcelService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * @author 州牧
 * @description excel表的导入和导出
 * @since 2022-05-05 20:05
 */
@Slf4j
@RestController
@RequestMapping("/excel")
@PreAuthorize("hasAuthority('managing_students')")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @UserLog(module = ModuleEnum.EXCEL,title = "导入学生名单",type = OperationEnum.IMPORT)
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,@RequestParam("grade") Long gradeId){
        log.info("{}",gradeId);
        excelService.upload(file,gradeId);
        return Result.success();
    }
    @UserLog(module = ModuleEnum.EXCEL,title = "导入教师名单",type = OperationEnum.IMPORT)
    @PostMapping("/uploadTeacher")
    public Result uploadTeacher(@RequestPart("file") MultipartFile file){
        excelService.uploadTeacher(file);
        return Result.success();
    }
/*不可返回值？很奇怪*/
@UserLog(module = ModuleEnum.EXCEL,title = "导入教师名单",type = OperationEnum.EXPORT)
@PostMapping("/downloadNote")
    public Result download(@RequestBody DownloadNoteDTO downloadNoteDTO,HttpServletResponse response){
        excelService.download(downloadNoteDTO,response);
        return Result.success();
    }

    @ApiLog
    @GetMapping("/downloadNote1")
    public Result download1(DownloadNoteDTOForGet downloadNoteDTOForGet, HttpServletResponse response){
        DownloadNoteDTO downloadNoteDTO = MyBeanUtils.copyBean(downloadNoteDTOForGet, DownloadNoteDTO.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (!Objects.isNull(downloadNoteDTOForGet.getEndTime())){
                downloadNoteDTO.setEndTime(simpleDateFormat.parse(downloadNoteDTOForGet.getEndTime()));
            }

            if(!Objects.isNull(downloadNoteDTOForGet.getStartTime())){
                downloadNoteDTO.setStartTime(simpleDateFormat.parse(downloadNoteDTOForGet.getStartTime()));
            }
           } catch (ParseException e) {
            e.printStackTrace();
        }

        excelService.download(downloadNoteDTO,response);
        return Result.success();
    }




}
