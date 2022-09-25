package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.sdk.SsoConstant;
import com.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import org.springframework.stereotype.Service;

@Service(SsoConstant.Method.HELLO_WORLD)
public class HelloWorldApi extends BaseApi<SsoBaseRequest> {
    @Override
    SsoResponse doService(SsoBaseRequest data) {
        return new SsoResponse().code(200).data("Hello World");
    }
}