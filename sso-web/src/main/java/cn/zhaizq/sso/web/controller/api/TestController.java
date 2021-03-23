package cn.zhaizq.sso.web.controller.api;

import cn.zhaizq.sso.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
public class TestController extends BaseController {
    @RequestMapping("/test")
    public String refresh_token() throws IOException {
        return "redirect:http://baidu.com";
    }
}