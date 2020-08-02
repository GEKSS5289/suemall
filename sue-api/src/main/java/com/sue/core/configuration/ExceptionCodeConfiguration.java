package com.sue.core.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sue
 * @date 2020/8/2 18:53
 */
@Component
@ConfigurationProperties(prefix = "error")
@PropertySource(value = "classpath:config/error-code.properties")
@Data
public class ExceptionCodeConfiguration {
    private Map<Integer,String> codes = new HashMap<>();
    public String getMessage(Integer code) {
        String msg = this.codes.get(code);
        return msg;
    }
}
