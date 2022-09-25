package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.controller.BaseController;
import com.zhaizq.sso.common.enums.RequestFormat;
import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.service.domain.entry.Application;
import com.zhaizq.sso.service.service.ApplicationService;
import com.zhaizq.sso.common.utils.ValidateUtil;
import com.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import com.zhaizq.sso.sdk.domain.request.SsoRequestHeader;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    private final static int TIMESTAMP_TIME_OUT = 60 * 1000;

    @Autowired
    private Map<String, BaseApi<?>> apiMap;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public SsoResponse action(@RequestBody(required = false) String body) {
        try {
            Object data = doAction(body);
            return new SsoResponse().code(200).data(data);
        } catch (BusinessException e) {
            return new SsoResponse().code(400).message(e.getMessage());
        } catch (Exception e) {
            log.error("系统内部错误", e);
            return new SsoResponse().code(500).message("系统内部错误");
        }
    }

    public Object doAction(String body) throws Exception {
        RequestFormat requestFormat = RequestFormat.valueOof(request.getContentType());
        if (requestFormat == null)
            throw new BusinessException("请求格式[" + request.getContentType() + "]不支持");

        SsoBaseRequest ssoRequest = requestFormat.parse(body, SsoBaseRequest.class);

        ValidateUtil.validate(ssoRequest);

        SsoRequestHeader header = ssoRequest.getHeader();

//        try (Jedis jedis = jedisPool.getResource()) {
//            if (jedis.setnx(header.getRequest_id(), "blah") != 0)
//                jedis.pexpire(header.getRequest_id(), TIMESTAMP_TIME_OUT);
//            else
//                throw new BusinessException("重复请求");
//        }

        if (System.currentTimeMillis() - header.getTimestamp() > TIMESTAMP_TIME_OUT) {
            throw new BusinessException("请求已过期");
        }

        Application application = applicationService.query(header.getApp_id());
        if (application == null)
            throw new BusinessException("应用[" + header.getApp_id() + "]未注册");

        // 验签
        try {
            if (!StringRsaUtil.verify(body, request.getHeader("sign"), application.getPublicKey()))
                throw new BusinessException("验签失败");
        } catch (Exception e) {
//            throw new BusinessException("验签失败");
        }
        // TODO 鉴权 (暂时不需要)

        BaseApi<?> api = apiMap.get(header.getMethod());
        if (api == null)
            throw new BusinessException("服务[" + header.getMethod() + "]不存在");

        ssoRequest = requestFormat.parse(body, api.getParamClass());
        return api.service(ssoRequest);
    }
}