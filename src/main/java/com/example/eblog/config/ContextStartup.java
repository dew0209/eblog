package com.example.eblog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eblog.entity.MCategory;
import com.example.eblog.service.MCategoryService;
import com.example.eblog.service.MPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

@Configuration
public class ContextStartup implements ApplicationRunner, ServletContextAware {
    @Autowired
    MCategoryService categoryService;

    @Autowired
    MPostService postService;

    ServletContext servletContext;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MCategory> categories = categoryService.list(new QueryWrapper<MCategory>().eq("status", 0));
        servletContext.setAttribute("categorys",categories);
        postService.initWeekRank();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
