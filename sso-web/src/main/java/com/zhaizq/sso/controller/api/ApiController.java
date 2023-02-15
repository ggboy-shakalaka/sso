package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.common.enums.RequestFormat;
import com.zhaizq.sso.common.enums.RequestFormatFactory;
import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.common.utils.ValidateUtil;
import com.zhaizq.sso.controller.BaseController;
import com.zhaizq.sso.sdk.domain.request.SsoRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.zhaizq.sso.service.domain.entry.Application;
import com.zhaizq.sso.service.service.ApplicationService;
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
    private Map<String, BaseApi2<?>> apiMap;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public SsoResponse action(@RequestBody(required = false) String body) {
        try {
            Object data = doAction(body);
            return SsoResponse.build(200, data, null);
        } catch (BusinessException e) {
            return SsoResponse.build(400, null, e.getMessage());
        } catch (Exception e) {
            log.error("系统内部错误", e);
            return SsoResponse.build(500, null, "系统内部错误");
        }
    }

    public Object doAction(String body) throws Exception {
        RequestFormat format = RequestFormatFactory.find(request.getContentType());
        String sign = request.getParameter("sign");
        SsoRequest ssoRequest = format.parse(body, SsoRequest.class);

        this.validateParams(ssoRequest);
        this.repeatRequest(ssoRequest);
        this.timeoutRequest(ssoRequest);
        this.invalidMethod(ssoRequest);
        this.invalidSign(ssoRequest, body, sign);

        BaseApi2<?> api = apiMap.get(ssoRequest.get_method());

        Object parse = format.parse(body, api.getParamClass());
        this.validateParams(ssoRequest);
        return api.service(ssoRequest);
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
        if (System.currentTimeMillis() - request.get_timestamp() > TIMESTAMP_TIME_OUT) {
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
        BaseApi2<?> api = apiMap.get(request.get_method());
        if (api == null)
            throw new BusinessException("服务[" + request.get_method() + "]不存在");

        // TODO 鉴权 (暂时不需要)
    }

    /**
     * 无效签名
     */
    private void invalidSign(SsoRequest request, String str, String sign) {
        Application application = applicationService.query(request.get_app());
        if (application == null || !application.isActive()) {
            throw new BusinessException("应用[" + request.get_app() + "]未注册");
        }

        // 验签
        try {
            if (!StringRsaUtil.verify(str, sign, application.getPublicKey()))
                throw new BusinessException("签名校验失败");
        } catch (Exception e) {
//            throw new BusinessException("验签失败");
        }
    }
}