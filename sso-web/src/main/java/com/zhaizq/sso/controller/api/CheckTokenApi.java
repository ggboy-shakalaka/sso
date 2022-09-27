package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.sdk.SsoConstant;
import com.zhaizq.sso.sdk.domain.SsoUser;
import com.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.zhaizq.sso.mapper.entry.User;
import com.zhaizq.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(SsoConstant.Method.CHECK_TOKEN)
public class CheckTokenApi extends BaseApi<SsoCheckTokenRequest> {
    @Autowired
    private LoginService loginService;

    @Override
    Object doService(SsoCheckTokenRequest data) {
        User user = loginService.getUserByToken(data.getToken());
        if (user == null) throw new BusinessException(400, "token不存在");

        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(user.getId());
        ssoUser.setName(user.getUserName());
        return SsoResponse.success(ssoUser);
    }
}