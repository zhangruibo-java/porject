package com.mr.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mr.config.FastClientImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Component
public class UploadService {


    private static Map <String,Boolean> allowsuffix;
    //静态块 可以初始化加载
    static {
        allowsuffix=new HashMap<>();
        allowsuffix.put("jpg",true);
        allowsuffix.put("jfif",true);
        allowsuffix.put("png",true);
        allowsuffix.put("gif",true);
    }
    //注入客户端类
    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImg(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        String[] hzArr=file.getOriginalFilename().split("\\.");
        String suffix=hzArr[hzArr.length-1];
        if(allowsuffix.get(suffix)==null){
            System.out.println("文件后缀不对");
        }
        if(!this.validImg(file)){
            System.out.println("文件不支持上传");
        }

        /*String nweFileName= UUID.randomUUID().toString()+"."+suffix;
        File img=new File("E:\\img\\"+nweFileName);
        try {
            file.transferTo(img);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        StorePath storePath = null;
        try {
            // storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(),hz,null);
            storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(),suffix,null);//存储的是缩略图
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(storePath.getGroup());
        System.out.println(storePath.getPath());
        System.out.println(storePath.getFullPath());



        return "http://image.b2c.com/"+storePath.getFullPath();
    }

    public boolean validImg(MultipartFile file){
        int len = 10;
        BufferedInputStream imgFile = null;
        try {
            imgFile = new BufferedInputStream(file.getInputStream());
            Image img;
            try {
                img = ImageIO.read(imgFile);
                System.out.println("解析结果"+!(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0));
            } catch (Exception e) {
                System.out.println("判断失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imgFile != null) {
                try {

                    imgFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
