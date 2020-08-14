package cn.zhaizq.sso.web.controller.api;

import cn.zhaizq.sso.sdk.SsoHelper;
import cn.zhaizq.sso.web.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.io.IOException;

@RestController
@RequestMapping("/api/haha")
public class HaHa extends BaseController {
    @RequestMapping("/test")
    public void refresh_token() throws IOException {
        System.out.println(SsoHelper.getSsoToken(request));
        SsoHelper.setSsoToken(response, "abcdefg");
    }
}