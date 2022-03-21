package com.example.eblog.controller;

import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MPost;
import org.apache.shiro.util.Assert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{

    @PostMapping("/jie-set/")
    public Result jieSet(Long id,Integer rank,String field){
        MPost post = postService.getById(id);
        Assert.notNull(post,"该帖子已被删除");
        if ("delete".equals(field)){
            postService.removeById(id);
        }else if("status".equals(field)){
            post.setRecommend(rank == 1);

        }else if("stick".equals(field)){
            post.setLevel(rank);
        }
        postService.updateById(post);
        return Result.success();
    }



}
