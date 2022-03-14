package com.example.eblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.eblog.vo.MCommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
@Component
public interface MCommentMapper extends BaseMapper<MComment> {

    IPage<MCommentVo> selectsComments(Page page,@Param(Constants.WRAPPER) QueryWrapper<MComment> wrapper);
}
