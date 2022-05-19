package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.ImagesNote;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @ApiLog
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,@RequestParam("id") Long id){

        imageService.upload(file,id);
        return Result.success();
    }

    /*
   批量上传
    */

    @PostMapping(value = "/uploadFiles")
    public Result uploadFiles(@RequestPart("files") MultipartFile[] files,@RequestParam("id") Long id) {

        imageService.upload(files,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<List<ImagesNote>> getImagesForNote(@PathVariable Long id){
        List<ImagesNote> list = imageService.getImagesForNote(id);
        return Result.success(list);
    }
    @ApiLog
    @GetMapping("/delete/{id}")
    public Result deleteByNoteId(@PathVariable Long id){
        imageService.deleteByNoteId(id);
        return Result.success();
    }
}
