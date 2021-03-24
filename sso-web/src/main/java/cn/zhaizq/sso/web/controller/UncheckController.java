package cn.zhaizq.sso.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uncheck/**")
public class UncheckController extends BaseController {
    @RequestMapping
    public String abc() {
        return request.getParameter("redirect");
    }
}
