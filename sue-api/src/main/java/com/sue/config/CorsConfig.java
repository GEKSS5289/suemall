package com.sue.config;

import org.springframework.aop.scope.ScopedObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author sue
 * @date 2020/8/1 11:24
 */
@Configuration
public class CorsConfig {
    public CorsConfig(){

    }
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config  = new CorsConfiguration();
        config.addAllowedOrigin("*");
        //是否允许携带cookie
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        //为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);

        //返回重新定义好的source
        return new CorsFilter(corsSource);
    }

}
