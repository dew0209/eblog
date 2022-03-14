package com.example.eblog.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eblog.entity.MPost;
import com.example.eblog.mapper.MPostMapper;
import com.example.eblog.service.MPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eblog.util.RedisUtil;
import com.example.eblog.vo.MPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
@Service
public class MPostServiceImpl extends ServiceImpl<MPostMapper, MPost> implements MPostService {
    @Autowired
    MPostMapper postMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public IPage<MPostVo> paging(Page page, Long categoryId, Long userId, Integer level, Boolean recommend, String order) {
        if(level == null)level = -1;
        QueryWrapper wrapper = new QueryWrapper<MPost>()
                .eq(categoryId != null,"category_id",categoryId)
                .eq(userId != null,"user_id",userId)
                .eq(level == 0,"level",0)
                .gt(level > 0,"level",level)
                .orderByDesc(order != null,order);
                ;
        return postMapper.selectPosts(page,wrapper);

    }

    @Override
    public MPostVo selectOnePost(QueryWrapper<MPost> wrapper) {

        return postMapper.selectOnePost(wrapper);
    }

    @Override
    public void initWeekRank() {
        //获取七天内文章
        List<MPost> posts = this.list(new QueryWrapper<MPost>().ge("created", DateUtil.offsetDay(new Date(), -7)).select("id,title,user_id,comment_count,view_count,created"));

        //初始化文章的总阅读量
        for(MPost post : posts){

            String key = "day:rank" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_FORMAT);
            redisUtil.zSet(key,post.getId(),post.getCommentCount());
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;
            redisUtil.expire(key,expireTime);
            //缓存文章的一些基本信息
            this.hashCachePostIdAndTitle(post,expireTime);

        }


        //做并集
        this.zunionAndStoreLast7DayForWeekRank();
    }

    @Override
    public void incrCommentCountAndUnionForWeekRank(Long postId, boolean inIncr) {
        String currentKey = "day:rank" + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        redisUtil.zIncrementScore(currentKey,postId,inIncr?1:-1);
        MPost post = this.getById(postId);
        long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
        long expireTime = (7 - between) * 24 * 60 * 60;
        hashCachePostIdAndTitle(post,expireTime);
        this.zunionAndStoreLast7DayForWeekRank();
    }

    @Override
    public void setViewCount(MPostVo vo) {
        String key = "rank:post:" + vo.getId();

        Integer viewCount = (Integer)redisUtil.hget(key, "post:viewCount");
        if(viewCount != null){
            vo.setViewCount(viewCount + 1);
        }else {
            vo.setViewCount(vo.getViewCount() + 1);
        }
        redisUtil.hset(key,"post:viewCount",vo.getViewCount());
    }

    private void zunionAndStoreLast7DayForWeekRank() {
        String currentKey = "day:rank" + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        List<String> otherKey = new ArrayList<>();
        String key = "week:rank";
        for(int i = -6;i < 0;i++){
            String other = "day:rank" + DateUtil.format(DateUtil.offsetDay(new Date(), i), DatePattern.PURE_DATE_FORMAT);
            otherKey.add(other);
        }
        redisUtil.zUnionAndStore(currentKey,otherKey,key);
    }

    private void hashCachePostIdAndTitle(MPost post, long expireTime) {
        String key = "rank:post:" + post.getId();
        boolean hasKey = redisUtil.hasKey(key);
        if (!hasKey){
            redisUtil.hset(key,"post:id",post.getId(),expireTime);
            redisUtil.hset(key,"post:title",post.getTitle(),expireTime);
            redisUtil.hset(key,"post:commentCount",post.getCommentCount(),expireTime);
            redisUtil.hset(key,"post:viewCount",post.getViewCount(),expireTime);
        }

    }
}
