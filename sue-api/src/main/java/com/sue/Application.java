package com.sue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import javax.persistence.Entity;

/**
 * @author sue
 * @date 2020/7/31 10:28
 */


@SpringBootApplication
@ComponentScan(basePackages={"com.sue","org.n3r.idworker"})
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
