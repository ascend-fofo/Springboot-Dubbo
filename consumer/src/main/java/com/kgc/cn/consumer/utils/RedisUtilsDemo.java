package com.kgc.cn.consumer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtilsDemo {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 增加一条数据
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * String类型获取
     * @param key
     * @return
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除
     * String... key里面可以有单个或多个字符串,当单个字符串进入方法后自动转化成数组，所以应该用array[0]
     * @param key
     */
    public void del(String... key){
        if (key != null && key.length > 0) {
            if (key.length == 1){
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 指定key失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean set_expire(String key, Object value, long time){
        set(key,value);
        try {
            if (time > 0) {
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key
     * @param delta"递增因子"
     * @return
     */
    public long incr(String key,long delta){
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 递减
     * @param key
     * @param delta"递减"
     * @return
     */
    public long decr(String key,long delta){
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,-delta);
    }
}
