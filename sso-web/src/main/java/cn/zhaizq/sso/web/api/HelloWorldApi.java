package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import org.springframework.stereotype.Service;

@Service(SsoConstant.Method.HELLO_WORLD)
public class HelloWorldApi extends BaseApi<SsoBaseRequest> {
    @Override
    Object doService(SsoBaseRequest data) {
        return "Hello World!\n" + data.getHeader().getApp_id();
    }
}