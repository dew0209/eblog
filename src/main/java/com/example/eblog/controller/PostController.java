package com.example.eblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.eblog.entity.MPost;
import com.example.eblog.vo.MCommentVo;
import com.example.eblog.vo.MPostVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController extends BaseController{
    @GetMapping("/category/{id:\\d}")
    public String category(@PathVariable("id")Long id){
        int pn = ServletRequestUtils.getIntParameter(req,"pn",1);
        req.setAttribute("currentCategoryId",id);
        req.setAttribute("pn",pn);
        return "/post/category";
    }
    @GetMapping("/post/{id:\\d}")
    public String detail(@PathVariable("id")Long id){
        MPostVo vo = postService.selectOnePost(new QueryWrapper<MPost>().eq("p.id",id));
        Assert.notNull(vo,"文章已经被删除");
        postService.setViewCount(vo);
        //分页 文章id 用户id 排序
        IPage<MCommentVo> results = commentService.paging(getPage(),vo.getId(),null,"created");
        req.setAttribute("post",vo);
        req.setAttribute("currentCategoryId",vo.getId());
        req.setAttribute("pageData",results);
        return "/post/detail";
    }
}
