package com.example.eblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MUserMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
public interface MUserMessageService extends IService<MUserMessage> {

    IPage paging(Page page, @Param(Constants.WRAPPER)QueryWrapper<MUserMessage> wrapper);

    void updateToReaded(List<Long> ids);
}
