package com.zhaizq.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
public class IndexController extends BaseController {
    @GetMapping
    public String index() {
        return "hello world!";
    }
}