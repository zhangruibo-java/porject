package com.mr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;


@Service
public class FileStaticService {
    //查询model数据
    @Autowired
    private GoodPageService goodPageService;
    //注入静态化模版 模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    //文件保存路径
    @Value("${b2c.thymeleaf.destPath}")
    private String destPath;// 保存的页面路径

    /**
     *创建html页面
     * @param id
     */
    public void createStaticHtml(Long id) throws FileNotFoundException, UnsupportedEncodingException {
        // 创建上下文context
        Context context = new Context();
        // 把数据加入上下文context 将页面需要的参数方放入到上下文中
        context.setVariables(this.goodPageService.getGoodInfo(id));
        // 创建最终文件输出流，指定文件夹目录，文件名 后缀 等 创建html
        File itemHtml =new File(destPath,id+".html");
        //创建输出流 指定格式
        PrintWriter printWriter = new PrintWriter(itemHtml,"UTF-8");
        // 利用thymeleaf模板引擎将上下文结合模版生成到指定到文件中
        templateEngine.process("item",context,printWriter);
    }
}
