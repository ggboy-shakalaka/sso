package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service(SsoConstant.Method.CHECK_TOKEN)
public class CheckTokenApi extends BaseApi<SsoCheckTokenRequest> {
    @Autowired
    private LoginService loginService;

    @Override
    Object doService(SsoCheckTokenRequest data) {
        User user = loginService.getUserByToken(data.getToken());
        if (user == null)
            return new SsoResponse().code(400);

        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(user.getId());
        ssoUser.setName(user.getUserName());
        return ssoUser;
    }
}