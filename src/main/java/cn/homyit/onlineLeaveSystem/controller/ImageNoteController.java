package cn.homyit.onlineLeaveSystem.controller;

import cn.homyit.onlineLeaveSystem.entity.DO.ImagesNote;
import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.log.ApiLog;
import cn.homyit.onlineLeaveSystem.log.UserLog;
import cn.homyit.onlineLeaveSystem.myEnum.ModuleEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OperationEnum;
import cn.homyit.onlineLeaveSystem.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
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

//    @UserLog(module = ModuleEnum.IMAGE,title = "上传凭证",type = OperationEnum.ADD)
    @ApiLog
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,@RequestParam("id") Long id){

        imageService.upload(file,id);
        return Result.success();
    }

    /*
   批量上传
    */
//    @UserLog(module = ModuleEnum.IMAGE,title = "上传凭证",type = OperationEnum.ADD)
    @PostMapping(value = "/uploadFiles")
    public Result uploadFiles(@RequestPart("files") MultipartFile[] files,@RequestParam("id") Long id) {

        imageService.upload(files,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<List<ImagesNote>> getImagesForNote(@PathVariable @NotNull(message = "id不为空") Long id){
        List<ImagesNote> list = imageService.getImagesForNote(id);
        return Result.success(list);
    }

//    @UserLog(module = ModuleEnum.IMAGE,title = "删除凭证",type = OperationEnum.DELETE)
    @ApiLog
    @GetMapping("/delete/{id}")
    public Result deleteByNoteId(@PathVariable Long id){
        imageService.deleteByNoteId(id);
        return Result.success();
    }
}
