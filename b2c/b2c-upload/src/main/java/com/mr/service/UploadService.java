package com.mr.service;

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


    /*
     *1 验证是否是图片
     *   1.1 验证后缀名 peg jpg ....
     *   1.2 验证内容 是否可以打开
     *   1.3 验证图片的宽高
     *2 验证完成 存储到硬盘 file.transferTo(); 硬盘(io 多,大的时候寿命会缩短)
     *   2.1 是否备份
     *   2.2 是否分布式
     *3 返回路径(url形式 可以直接在页面展示)
     *   3.1tomcat(应用服务器(动态,并发不高)) web服务器(nginx )
     * */
    private static Map<String,Boolean> allowSuffix;
    /*静态块 可以初始化加载*/
    static{
        allowSuffix =new HashMap<>();
        allowSuffix.put("png",true);
        allowSuffix.put("jpg",true);
        allowSuffix.put("gip",true);
        allowSuffix.put("bmp",true);
    }

    public String uploadImg(MultipartFile file) {

        String[] hzArr = file.getOriginalFilename().split("\\.");
        String suffix = hzArr[hzArr.length - 1];
        System.out.println(file.getOriginalFilename());
      // 1.1 验证后缀名 peg jpg ....
        if (allowSuffix.get(suffix)==null) {
            System.out.println("文件后缀不对");
        }
        //1.2 验证内容 是否可以打开
            if (!this.validImg(file)){
                System.out.println("文件内容不对");
            }
            String newFileName = UUID.randomUUID().toString()+"."+suffix;
            //图片 保存 地址
        File img = new File("E:\\mr\\img\\"+newFileName);
        try{
            //保存
            file.transferTo(img);
        }catch (IOException e){
            e.printStackTrace();
        }
        //返回路径(url形式 可以直接在页面展示)

    return "http://img.b2c.com/"+newFileName;
    }

    public  boolean validImg(MultipartFile file){
        int len = 10;
        BufferedInputStream imgFile = null;
        try {
            imgFile = new BufferedInputStream(file.getInputStream());
            Image img;
            try {
                img = ImageIO.read(imgFile);
              return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
            } catch (Exception e) {
                System.out.println("cache");
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
