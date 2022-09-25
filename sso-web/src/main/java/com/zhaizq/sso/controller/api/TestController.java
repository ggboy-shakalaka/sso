package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class TestController extends BaseController {
    @RequestMapping("/test")
    public String refresh_token() throws IOException {
        return "redirect:http://baidu.com";
    }
}