package com.example.eblog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MCategory;
import com.example.eblog.entity.MPost;
import com.example.eblog.entity.MUserCollection;
import com.example.eblog.util.ValidationUtil;
import com.example.eblog.vo.MCommentVo;
import com.example.eblog.vo.MPostVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

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
    @ResponseBody
    @PostMapping("/collection/find")
    public Result collectionFind(Long pid){
        int count = collectionService.count(new QueryWrapper<MUserCollection>()
                .eq("user_id", getProFile())
                .eq("post_id", pid)
        );
        return Result.success(MapUtil.of("collection",count > 0));
    }
    @ResponseBody
    @PostMapping("/collection/add")
    public Result collectionAdd(Long pid){
        MPost post = postService.getById(pid);
        Assert.isTrue(post != null,"该帖子已被删除");
        int count = collectionService.count(new QueryWrapper<MUserCollection>()
                .eq("user_id", getProFile())
                .eq("post_id", pid)
        );
        if (count > 0){
            return Result.fail("你已经收藏过了");
        }
        MUserCollection userCollection = new MUserCollection();
        userCollection.setUserId(getProFileId());
        userCollection.setPostId(pid);
        userCollection.setCreated(new Date());
        userCollection.setPostId(post.getUserId());
        collectionService.save(userCollection);
        return Result.success();
    }
    @ResponseBody
    @PostMapping("/collection/remove")
    public Result collectionRemove(Long pid){
        MPost post = postService.getById(pid);
        Assert.isTrue(post != null,"该帖子已被删除");
        collectionService.remove(new QueryWrapper<MUserCollection>()
                .eq("user_id", getProFile())
                .eq("post_id", pid));
        return Result.success();
    }
    @GetMapping("/post/edit")
    public String edit(){
        String id = req.getParameter("id");
        if(!StringUtils.isEmpty(id)){
            MPost post = postService.getById(id);
            Assert.isTrue(post != null,"帖子已经删除");
            System.out.println(post.getUserId());
            System.out.println(getProFileId());
            Assert.isTrue(post.getUserId() == getProFileId(),"没有权限操作此文章");
            req.setAttribute("post",post);
        }
        List<MCategory> list = categoryService.list();
        System.out.println(list);
        for (MCategory mCategory : list) {
            System.out.println(mCategory.getId());
        }
        req.setAttribute("categories",list);
        return "/post/edit";
    }
    //post/submit
    @PostMapping("/post/submit/")
    public Result submit(MPost post){
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(post);
        if (validResult.hasErrors()){
            return Result.fail(validResult.getErrors());
        }
        if(post.getId() == null) {
            post.setUserId(getProFileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(null);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            postService.save(post);

        } else {
            MPost tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId().longValue() == getProFileId().longValue(), "无权限编辑此文章！");

            tempPost.setTitle(post.getTitle());
            tempPost.setContent(post.getContent());
            tempPost.setCategoryId(post.getCategoryId());
            postService.updateById(tempPost);
        }

        return Result.success().action("/post/" + post.getId());
    }

}
