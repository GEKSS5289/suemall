package com.sue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sue
 * @date 2020/8/8 15:29
 */

@RestController
@RequestMapping("redis")
public class RedisController {


    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/set")
    public Object set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
        return redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/get")
    public String get(String key){
        return (String)redisTemplate.opsForValue().get(key);
    }
    @GetMapping("/delete")
    public Object delete(String key){
        redisTemplate.delete(key);
        return "OK";
    }

}
