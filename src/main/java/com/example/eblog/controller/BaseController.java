package com.example.eblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.service.MCommentService;
import com.example.eblog.service.MPostService;
import com.example.eblog.service.MUserService;
import com.example.eblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    MPostService postService;
    @Autowired
    MCommentService commentService;
    @Autowired
    MUserService userService;
    public Page getPage(){
        int pn = ServletRequestUtils.getIntParameter(req,"pn",1);
        int size = ServletRequestUtils.getIntParameter(req,"size",2);
        return new Page(pn,size);
    }
    public AccountProfile getProFile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
    protected Long getProFileId(){
        return getProFile().getId();
    }
}
