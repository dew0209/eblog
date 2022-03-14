package com.example.eblog.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eblog.entity.MPost;
import com.example.eblog.service.MPostService;
import com.example.eblog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class ViewCountSyncTask {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MPostService postService;
    @Scheduled(cron = "0/5 * * * * *")
    public void task(){
        System.out.println("执行之前");
        Set<String> keys = redisTemplate.keys("rank:post:*");
        List<String> ids = new ArrayList<>();
        for(String key : keys){
            String postId = key.substring("rank:post:".length());
            if(redisUtil.hHasKey(key,"post:viewCount")){
                ids.add(postId);
            }
        }
        if(ids.isEmpty())return;
        List<MPost> posts = postService.list(new QueryWrapper<MPost>().in("id", ids));
        posts.stream().forEach((post) -> {
            Integer viewCount = (Integer)redisUtil.hget("rank:post:" + post.getId(),"post:viewCount");
            post.setViewCount(viewCount);
        });
        if(posts.isEmpty())return;
        boolean isSucc = postService.updateBatchById(posts);
        if(isSucc){
            ids.stream().forEach((id)->{
                redisUtil.hdel("rank:post:" + id,"post:viewCount");
            });
        }
        System.out.println("执行了");
    }
}
