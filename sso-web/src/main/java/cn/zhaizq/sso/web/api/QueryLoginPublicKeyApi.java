package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoQueryLoginPublicKeyRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.service.LoginService;
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
        return new SsoResponse<String>().code(200).data(publicKey);
    }
}