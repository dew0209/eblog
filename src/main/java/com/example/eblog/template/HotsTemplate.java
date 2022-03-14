package com.example.eblog.template;

import com.example.eblog.common.templates.DirectiveHandler;
import com.example.eblog.common.templates.TemplateDirective;
import com.example.eblog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HotsTemplate extends TemplateDirective {
    @Autowired
    RedisUtil redisUtil;
    @Override
    public String getName() {
        return "hots";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        String weekRankKey = "week:rank";
        List<Map> hotpots = new ArrayList<>();
        Set<ZSetOperations.TypedTuple> typedTuples = redisUtil.getZSetRank(weekRankKey, 0, 6);
        for (ZSetOperations.TypedTuple typedTuple : typedTuples) {
            Map<String,Object> map = new HashMap<>();
            Object value = typedTuple.getValue();//postid
            String postKey = "rank:post:" + value;
            map.put("id",value);
            map.put("title",redisUtil.hget(postKey,"post:title"));
            map.put("commentCount",typedTuple.getScore());
            hotpots.add(map);
        }
        handler.put(RESULTS,hotpots).render();
    }
}
