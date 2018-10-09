package com.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 陈圣融 on 2017-07-23.
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @RequestMapping("/oauth2Test")
    public String oauth2Test() {
        return "test success";
    }
}
