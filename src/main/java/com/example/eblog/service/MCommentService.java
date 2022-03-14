package com.example.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eblog.vo.MCommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
public interface MCommentService extends IService<MComment> {

    IPage<MCommentVo> paging(Page page, Long postId, Long userId, String order);
}
