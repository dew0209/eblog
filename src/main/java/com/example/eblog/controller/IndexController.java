package com.example.eblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController{
    @GetMapping({"","/","index"})
    public String index(){
        //分页信息 分类 用户信息 置顶 精选 排序
        IPage results = postService.paging(getPage(),null,null,null,null,"created");
        req.setAttribute("currentCategoryId",0);
        req.setAttribute("pageData",results);
        return "index";
    }
}
