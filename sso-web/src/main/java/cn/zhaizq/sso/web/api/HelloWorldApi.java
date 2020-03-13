package cn.zhaizq.sso.web.api;

import org.springframework.stereotype.Service;

@Service("helloWorld")
public class HelloWorldApi extends BaseApi<BaseApiParam> {
    @Override
    Object doService(BaseApiParam data) {
        return "Hello World: " + data.getAppId();
    }
}
