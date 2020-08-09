package com.sue.controller;

import com.sue.utils.RedisOperator;
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


//    @Autowired
//    private RedisTemplate redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key,String value){
        redisOperator.set(key,value);
        return redisOperator.get(key);
    }

    @GetMapping("/get")
    public String get(String key){

        return redisOperator.get(key);
    }
    @GetMapping("/delete")
    public Object delete(String key){
        redisOperator.del(key);
        return "OK";
    }

}
