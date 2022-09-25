package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.sdk.SsoConstant;
import com.zhaizq.sso.sdk.domain.request.SsoQueryLoginPublicKeyRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.zhaizq.sso.service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service(SsoConstant.Method.QUERY_LOGIN_PUBLIC_KEY)
public class QueryLoginPublicKeyApi extends BaseApi<SsoQueryLoginPublicKeyRequest> {
    @Autowired
    private LoginService loginService;

    @Override
    Object doService(SsoQueryLoginPublicKeyRequest data) throws NoSuchAlgorithmException {
        String publicKey = loginService.getPublicKeyByName(data.getName());
        return new SsoResponse().code(200).data(publicKey);
    }
}