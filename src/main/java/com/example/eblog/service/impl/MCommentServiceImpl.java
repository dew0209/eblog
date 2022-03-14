package com.example.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MComment;
import com.example.eblog.mapper.MCommentMapper;
import com.example.eblog.service.MCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eblog.vo.MCommentVo;
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
public class MCommentServiceImpl extends ServiceImpl<MCommentMapper, MComment> implements MCommentService {
    @Autowired
    MCommentMapper commentMapper;
    @Override
    public IPage<MCommentVo> paging(Page page, Long postId, Long userId, String order) {

        return commentMapper.selectsComments(page,new QueryWrapper<MComment>().eq(postId != null,"post_id",postId).eq(userId != null,"user_id",userId).orderByDesc(order != order,order));
    }
}
