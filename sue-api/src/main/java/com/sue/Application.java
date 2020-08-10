package com.sue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author sue
 * @date 2020/7/31 10:28
 */

@SpringBootApplication
@MapperScan(basePackages = "com.sue.mapper")
@ComponentScan(basePackages = {"com.sue","org.n3r.idworker"})
@EnableScheduling
//@EnableRedisHttpSession //开启使用redis作为springsession
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
