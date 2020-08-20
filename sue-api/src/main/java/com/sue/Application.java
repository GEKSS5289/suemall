package com.sue;

import com.sue.jvm.objectpool.datasource.DataSourceEnpoint;
import com.sue.jvm.objectpool.datasource.SueDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

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
    @Bean
    @Primary
    public DataSource dataSource(){
        return new SueDataSource();
    }

    @Bean
    public DataSourceEnpoint dataSourceEnpoint(){
        DataSourceEnpoint dataSourceEnpoint = new DataSourceEnpoint((SueDataSource) this.dataSource());
        return dataSourceEnpoint;
    }
}
