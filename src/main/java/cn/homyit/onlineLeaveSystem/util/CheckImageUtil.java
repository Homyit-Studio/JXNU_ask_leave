package cn.homyit.onlineLeaveSystem.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author 州牧
 * @description
 * @since 2022-05-19 11:09
 */
public class CheckImageUtil {

    static final String IMG_FORMAT = "png_jpg_bmp_gif_tif_jpeg_PNG_JPG_BMP_GIF_TIF_JPEG";
    static final String IMG_FORMAT_NUMBER = "8950_ffd8_424d_4749_4d4d_4949";
    /**
     * 针对图片内容的格式效验。
     * 分别以：
     * 1：判断后缀名的方式判断是否为图片
     * 2：以魔术数字进行判断
     * 3：以imageIO流的方式验证是否为图片
     *
     * @param mFile
     * @return boolean
     * @author lihao
     */

    public static boolean isImage(MultipartFile mFile) {
        File file = null;
        InputStream is = null;
        Image img = null;
        byte[] bt = new byte[2];
        try {
            file = File.createTempFile("tmp", null);
            mFile.transferTo(file);
            is = new FileInputStream(file);
            is.read(bt);
            img = ImageIO.read(file);
        } catch (IOException e) {
            return false;
        }

        //获取文件后缀进行判断
        String suffix = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf(".") + 1);
        if (IMG_FORMAT.indexOf(suffix) == -1) {
            return false;
        }

        //以魔术数字进行判断
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bt.length; i++) {
            int v = bt[i] & 0xFF;//byte to int
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        System.out.println(stringBuilder.toString());
        if (IMG_FORMAT_NUMBER.indexOf(stringBuilder.toString()) == -1) {
            return false;
        }

        //以imageIO流的方式对图片进行格式检查
        if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
            return false;
        }
        if(file.exists()){file.delete();}
        return true;
    }


}
