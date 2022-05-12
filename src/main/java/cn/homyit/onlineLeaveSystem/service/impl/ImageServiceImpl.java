package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.eneity.DO.ImagesNote;
import cn.homyit.onlineLeaveSystem.mapper.ImageMapper;
import cn.homyit.onlineLeaveSystem.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 州牧
 * @description
 * @since 2022-05-12 18:53
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Value("${upload.resourcePath}")
    private String resourcePath;

    @Value("${upload.urlPath}")
    private String urlPath;


    @Override
    public void upload(MultipartFile file) {
        log.info("准备上传到云服务器的{}目录",resourcePath);
        String temp = UUID.randomUUID() + file.getOriginalFilename();
        String uploadUrl = resourcePath+temp;

        System.out.println(uploadUrl);
        File uploadFile = new File(uploadUrl);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片上传失败");
        }

        String dbUrl = urlPath + temp;
        ImagesNote imagesNote = new ImagesNote();
        imagesNote.setUrl(dbUrl);
        imageMapper.insert(imagesNote);


    }
}
