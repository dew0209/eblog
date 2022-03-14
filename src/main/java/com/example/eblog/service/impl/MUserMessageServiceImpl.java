package com.example.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MUserMessage;
import com.example.eblog.mapper.MUserMessageMapper;
import com.example.eblog.service.MUserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
@Service
public class MUserMessageServiceImpl extends ServiceImpl<MUserMessageMapper, MUserMessage> implements MUserMessageService {
    @Autowired
    MUserMessageMapper userMessageMapper;
    @Override
    public IPage paging(Page page, QueryWrapper<MUserMessage> wrapper) {
        return userMessageMapper.selectMessages(page,wrapper);
    }
}
