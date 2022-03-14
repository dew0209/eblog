package com.example.eblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eblog.vo.MPostVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
public interface MPostService extends IService<MPost> {

    IPage paging(Page page, Long categoryId, Long userId, Integer level, Boolean recommend, String order);

    MPostVo selectOnePost(QueryWrapper<MPost> id);

    void initWeekRank();

    void incrCommentCountAndUnionForWeekRank(Long postId,boolean inIncr);

    void setViewCount(MPostVo vo);
}
