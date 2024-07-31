package com.bless.paysystemmanager.ctrl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-07-31 15:55
 */
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
