package com.zhaizq.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController extends BaseController {
    @PostMapping
    public String index() {
        return "hello world!";
    }
}