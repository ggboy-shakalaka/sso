package com.zhaizq.sso.controller;

import com.alibaba.fastjson2.JSON;
import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.common.utils.ValidateUtil;
import com.zhaizq.sso.sdk.StringRsaUtil;
import com.zhaizq.sso.sdk.domain.SsoRequest;
import com.zhaizq.sso.sdk.domain.SsoResponse;
import com.zhaizq.sso.service.domain.entry.Application;
import com.zhaizq.sso.service.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    private final static int TIMESTAMP_TIME_OUT = 60 * 1000;

    @Autowired
    private ApiMethodManager apiMethodManager;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public SsoResponse action(@RequestBody(required = false) String body) {
        try {
            String sign = request.getParameter("sign");
            SsoRequest ssoRequest = JSON.parseObject(body, SsoRequest.class);

//            this.validateParams(ssoRequest);
//            this.repeatRequest(ssoRequest);
//            this.timeoutRequest(ssoRequest);
            this.invalidMethod(ssoRequest);
//            this.invalidSign(ssoRequest, body, sign);

            // TODO parse params
            Object data = apiMethodManager.invoke(ssoRequest.getMethod(), ssoRequest.getParams() == null ? null : ssoRequest.getParams().toString());

            return SsoResponse.build(200, data, null);
        } catch (BusinessException e) {
            return SsoResponse.build(400, null, e.getMessage());
        } catch (Throwable e) {
            log.error("系统内部错误", e);
            return SsoResponse.build(500, null, "系统内部错误");
        }
    }

    /**
     * 参数校验
     */
    private void validateParams(SsoRequest request) {
        ValidateUtil.validate(request);
    }

    /**
     * 过期请求
     */
    private void timeoutRequest(SsoRequest request) {
        if (System.currentTimeMillis() - request.getTimestamp() > TIMESTAMP_TIME_OUT) {
            throw new BusinessException("请求已过期");
        }
    }

    /**
     * 防重提交
     */
    private void repeatRequest(SsoRequest request) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            if (jedis.setnx(header.getRequest_id(), "blah") != 0)
//                jedis.pexpire(header.getRequest_id(), TIMESTAMP_TIME_OUT);
//            else
//                throw new BusinessException("重复请求");
//        }
    }

    /**
     * 无效接口
     */
    private void invalidMethod(SsoRequest request) {
        if (!apiMethodManager.exists(request.getMethod()))
            throw new BusinessException("服务[" + request.getMethod() + "]不存在");

        // TODO 鉴权 (暂时不需要)
    }

    /**
     * 无效签名
     */
    private void invalidSign(SsoRequest request, String str, String sign) {
        Application application = applicationService.query(request.getApp());
        if (application == null || !application.isActive()) {
            throw new BusinessException("应用[" + request.getApp() + "]未注册");
        }

        // 验签
        try {
            if (!StringRsaUtil.verify(str, sign, application.getPublicKey()))
                throw new BusinessException("签名校验失败");
        } catch (Exception e) {
            throw new BusinessException("验签失败");
        }
    }
}