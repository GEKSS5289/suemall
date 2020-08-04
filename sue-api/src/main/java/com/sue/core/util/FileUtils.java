package com.sue.core.util;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.exception.usercenterexception.UserCenterException;
import com.sue.utils.DateUtil;
import com.sue.utils.IMOOCJSONResult;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sue
 * @date 2020/8/4 12:14
 */
@Data
public class FileUtils {

    private static String fileName;
    private static String fileSpace = BaseController.IMAGE_USER_FACE_LOCATION;
    private static String filePath = BaseController.IMAGES_ERVER_URL;
    private static String uploadPathPrefix = File.separator;
    private static String pathValue;
    private static String suffix;
    private static MultipartFile file;
    private static FileOutputStream fileOutputStream;
    private static InputStream inputStream;


    public static String saveFile(MultipartFile file,String pathValue){
        try {
            if (file != null) {
                //获取文件上传名称
                fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    //imooc.jpg -> ["imooc","jpg"]
                    //获取文件后缀
                    if(!checkFileSuffix(suffix)){
                        throw new UserCenterException(12000);
                    }
                    //face-{userid}.jpg
                    //文件名重组
                    String newFileName = "face-" + pathValue + "." + suffix;
                    //上传头像最终保存地址
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    //用于提供给web服务访问的地址
                    uploadPathPrefix += ("/"+newFileName);
                    //构建文件对象
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return filePath + uploadPathPrefix + "?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
    }


    private static boolean checkFileSuffix(String suffix){
        String[] split = fileName.split("\\.");
        suffix = split[split.length - 1];
        if(!suffix.equalsIgnoreCase("png")&&
                !suffix.equalsIgnoreCase("jpg")&&
                !suffix.equalsIgnoreCase("jpeg")
        ){
            return false;
        }
        return true;
    }
}
