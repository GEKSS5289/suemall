package com.sue.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object Hello(){
        return "sue";
    }
}
