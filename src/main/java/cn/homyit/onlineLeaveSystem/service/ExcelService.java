package cn.homyit.onlineLeaveSystem.service;

import cn.homyit.onlineLeaveSystem.entity.DTO.DownloadNoteDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {
    void upload(MultipartFile file,Long gradeId);

    void uploadTeacher(MultipartFile file);

    void download(DownloadNoteDTO downloadNoteDTO, HttpServletResponse response);
}
