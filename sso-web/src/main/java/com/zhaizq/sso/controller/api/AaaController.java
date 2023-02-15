package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.sdk.domain.SsoUser;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AaaController {
    @ApiMethod("/hello")
    public String hello() {
        System.out.println(123);
        return "hi";
    }

    @RequestMapping("/checkToken")
    public SsoResponse checkToken(String token) {
        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(1);
        ssoUser.setName("张三");
        return new SsoResponse().code(200).data(ssoUser);
    }
}