package cn.homyit.onlineLeaveSystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    void upload(MultipartFile file);

    void uploadTeacher(MultipartFile file);

}
