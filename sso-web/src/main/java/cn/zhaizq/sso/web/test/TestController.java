package cn.zhaizq.sso.web.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/api")
    public Param action(@RequestBody(required = false) Param Param) {
        System.out.println("12345");
        return new Param();
    }

    @Getter
    @Setter
    public static class Param {
        private String msg = "haha";
    }
}