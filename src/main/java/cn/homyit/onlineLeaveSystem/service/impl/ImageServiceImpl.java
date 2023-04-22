package cn.homyit.onlineLeaveSystem.service.impl;

import cn.homyit.onlineLeaveSystem.entity.DO.ImagesNote;
import cn.homyit.onlineLeaveSystem.exception.BizException;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.mapper.ImageMapper;
import cn.homyit.onlineLeaveSystem.service.ImageService;
import cn.homyit.onlineLeaveSystem.util.CheckImageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
    public void upload(MultipartFile file,Long id) {
        System.out.println(urlPath);
//        boolean isImage = CheckImageUtil.isImage(file);
//        if (!isImage){
//            throw new BizException(ExceptionCodeEnum.NOT_IMAGE);
//        }
        log.info("准备上传到云服务器的{}目录",resourcePath);
        String temp = UUID.randomUUID() + file.getOriginalFilename();
        String uploadUrl = resourcePath+temp;

        log.info("拼接字符串{}",uploadUrl);
        File uploadFile = new File(uploadUrl);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException(ExceptionCodeEnum.UPLOAD_ERROR);
        }

        String dbUrl = urlPath + temp;
        ImagesNote imagesNote = new ImagesNote();
        imagesNote.setUrl(dbUrl);
        imagesNote.setNoteId(id);
        imageMapper.insert(imagesNote);


    }

    @Override
    public void upload(MultipartFile[] files,Long id) {
        for (MultipartFile file : files) {
            upload(file,id);
        }
    }

    @Override
    public void uploadFilesAdd(MultipartFile[] files, Long discussionId) {
        for (MultipartFile file : files) {
            String temp = UUID.randomUUID() + file.getOriginalFilename();
            String uploadUrl = resourcePath+temp;
            File uploadFile = new File(uploadUrl);
            try {
                file.transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BizException(ExceptionCodeEnum.UPLOAD_ERROR);
            }

            String dbUrl = urlPath + temp;
            ImagesNote imagesNote = new ImagesNote();
            imagesNote.setUrl(dbUrl);
            imagesNote.setDiscussionId(discussionId);
            imageMapper.insert(imagesNote);
        }
    }

    @Override
    public List<ImagesNote> getImagesForadd(Long discussionId) {
        List<ImagesNote> imagesNotes = imageMapper.selectList(new QueryWrapper<ImagesNote>().eq("discussion_id", discussionId));
        if (CollectionUtils.isEmpty(imagesNotes)){
            throw new BizException(ExceptionCodeEnum.NO_IMAGES);
        }
        return imagesNotes;
    }

    @Override
    public List<ImagesNote> getImagesForNote(Long id) {
        List<ImagesNote> imagesNotes = imageMapper.selectList(new QueryWrapper<ImagesNote>().eq("note_id", id));
        if (CollectionUtils.isEmpty(imagesNotes)){
            throw new BizException(ExceptionCodeEnum.NO_IMAGES);
        }
        return imagesNotes;
    }

    @Override
    public void deleteByNoteId(Long id) {
        QueryWrapper<ImagesNote> wrapper = new QueryWrapper<>();
        wrapper.eq("note_id",id);
        List<ImagesNote> imagesNotes = imageMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(imagesNotes)){
            return;
        }
        for (ImagesNote imagesNote : imagesNotes) {
            String url = imagesNote.getUrl();
            String name = new File(url).getName();
            log.info("删除{}图片",name);
            new File(resourcePath+name).delete();
        }

        imageMapper.delete(wrapper);
    }


}
