package com.text;

import com.mr.GoodApplication;
import com.mr.bo.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.fegin.GoodClient;
import com.mr.service.FileStaticService;
import com.mr.service.GoodPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { GoodApplication.class})
public class CreatePageTest {

    @Autowired
    private GoodPageService goodPageService;

    @Autowired
    private GoodClient goodClient;

    @Autowired
    private FileStaticService fileStaticService;

   @Test
    public void createPage(){
        //查询spu分页数据
        PageResult<SpuBo> spuPage=goodClient.querySpuByPage("",0,200,"true");
        spuPage.getItems().forEach(spuBo -> {
            try {
                //循环创建静态文件
                fileStaticService.createStaticHtml(spuBo.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void createPage2() throws FileNotFoundException, UnsupportedEncodingException {
        //查询spu分页数据
       /* PageResult<SpuBo> spuPage=goodClient.querySpuByPage("",0,200,"true");
        spuPage.getItems().forEach(spuBo -> {
            try {
                //循环创建静态文件
                fileStaticService.createStaticHtml(spuBo.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
        System.out.println("测试成功");
        fileStaticService.createStaticHtml(154L);
    }

}
