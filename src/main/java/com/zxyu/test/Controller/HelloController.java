package com.zxyu.test.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {
    @RequestMapping("/hello")
    private String index(){
        return "hello world!";
    }
}
