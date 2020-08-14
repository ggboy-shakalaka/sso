package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import org.springframework.stereotype.Service;

@Service(SsoConstant.Method.HELLO_WORLD)
public class HelloWorldApi extends BaseApi<SsoBaseRequest> {
    @Override
    SsoResponse doService(SsoBaseRequest data) {
        return new SsoResponse().code(200).data("Hello World");
    }
}