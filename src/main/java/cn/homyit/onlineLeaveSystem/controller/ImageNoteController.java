package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.eneity.VO.Result;
import cn.homyit.onlineLeaveSystem.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 州牧
 * @description 关于假条的凭证图像
 * @since 2022-05-12 18:52
 */
@RestController
@RequestMapping("/image")
public class ImageNoteController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file){

        imageService.upload(file);
        return Result.success();
    }

}
