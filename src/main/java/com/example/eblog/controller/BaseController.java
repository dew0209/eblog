package com.example.eblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MUserMessage;
import com.example.eblog.service.*;
import com.example.eblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    WsService wsService;
    @Autowired
    MCategoryService categoryService;
    @Autowired
    MUserCollectionService collectionService;
    @Autowired
    MUserMessageService messageService;
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
