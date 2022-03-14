package com.example.eblog.template;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.common.templates.DirectiveHandler;
import com.example.eblog.common.templates.TemplateDirective;
import com.example.eblog.service.MPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsTemplate extends TemplateDirective {
    @Autowired
    MPostService postService;
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer level = handler.getInteger("level");
        Integer pn = handler.getInteger("pn", 1);
        Integer size = handler.getInteger("size", 2);
        Long categoryId = handler.getLong("categoryId");

        IPage results = postService.paging(new Page(pn, size), categoryId, null, level, null, "created");
        handler.put(RESULTS,results).render();
    }
}
