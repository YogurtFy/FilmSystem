package com.cqu.filmsystem.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;




@Configuration
public class RedisUtils implements ApplicationContextAware {

    private static StringRedisTemplate stringRedisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    public static String get( String key )
    {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public static void set( String key , String value ){
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public static void setTTl( String key , String value,int time ){
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public static void del(String key)
    {
        stringRedisTemplate.delete(key);
    }

}
