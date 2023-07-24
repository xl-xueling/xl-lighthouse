package com.dtstep.lighthouse.web.webutil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    public <T> void setEntity(String key,T value,long expireSeconds){
        redisTemplate.opsForValue().set(key, JsonUtil.toJSONString(value), expireSeconds, TimeUnit.SECONDS);
    }

    public <T> T getEntity(String key,Class<T> t){
        Object data = get(key);
        if(data == null){
            return null;
        }
        return JsonUtil.toJavaObject(data.toString(),t);
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    public void expire(String key, long expireSeconds) {
        redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                List list = CollectionUtils.arrayToList(key);
                redisTemplate.delete(list);
            }
        }
    }
    
    public <T> void setList(String key,List<T> valueList,long expireSeconds){
        Validate.notNull(valueList);
        set(key, JsonUtil.toJSONString(valueList),expireSeconds);
    }

    public <T> List<T> getList(String key, Class<T> t){
        Object data = get(key);
        if(data == null){
            return null;
        }
        return JsonUtil.toJavaObjectList(data.toString(),t);
    }
}
