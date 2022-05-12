package cn.homyit.onlineLeaveSystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void upload(MultipartFile file);
}
