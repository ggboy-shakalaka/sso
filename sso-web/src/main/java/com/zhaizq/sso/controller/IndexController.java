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
    @PostMapping
    public String index() {
        return "hello world!";
    }

    @GetMapping("/uncheck/refreshToken")
    public void refreshToken() throws IOException {
        String redirect = request.getParameter("redirect");
        String path = new URIBuilder(URI.create(redirect)).addParameter("sso.token", "123456").toString();
        response.sendRedirect(path);
    }
}