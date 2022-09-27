package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.common.enums.RequestFormat;
import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.common.utils.ValidateUtil;
import com.zhaizq.sso.controller.BaseController;
import com.zhaizq.sso.mapper.entry.Application;
import com.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.zhaizq.sso.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    private final static int TIMESTAMP_TIME_OUT = 60 * 1000;

    @Autowired
    private Map<String, BaseApi<?>> apiMap;
    @Autowired
    private ApiMethodHandler apiMethodHandler;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public SsoResponse action(@RequestBody(required = false) String body) {
        try {
            Object data = doAction(body);
            return SsoResponse.success(data);
        } catch (BusinessException e) {
            log.warn("业务异常, code: {}, message: {}", e.getCode(), e.getMessage());
            return SsoResponse.create(400, e.getMessage(), null);
        } catch (Exception e) {
            log.error("系统异常", e);
            return SsoResponse.create(500, "系统内部错误", null);
        }
    }

    @GetMapping("/test")
    public Object action2(@RequestBody(required = false) String body) throws InvocationTargetException, IllegalAccessException {
        ApiMethodHandler.MethodAccessor methodAccessor = apiMethodHandler.getMethodAccessor("aabbccc");
        return methodAccessor.invoke(ApiMethodHandler.fastjsonArgument(body));
    }

    @ApiMethod("aabbccc")
    public String aabbccc(String str, String str2) {
        return "hi: " + str;
    }

    public Object doAction(String body) throws Exception {
        RequestFormat requestFormat = RequestFormat.getByType(request.getContentType());
        if (requestFormat == null)
            throw new BusinessException("请求格式[" + request.getContentType() + "]不支持");

        SsoBaseRequest ssoRequest = requestFormat.parse(body, SsoBaseRequest.class);

        ValidateUtil.validate(ssoRequest);

//        try (Jedis jedis = jedisPool.getResource()) {
//            if (jedis.setnx(header.getRequest_id(), "blah") != 0)
//                jedis.pexpire(header.getRequest_id(), TIMESTAMP_TIME_OUT);
//            else
//                throw new BusinessException("重复请求");
//        }

        if (System.currentTimeMillis() - ssoRequest.getTimestamp() > TIMESTAMP_TIME_OUT) throw new BusinessException("请求已过期");

        Application application = applicationService.query(ssoRequest.getAppId());
        if (application == null) throw new BusinessException("应用[" + ssoRequest.getAppId() + "]未注册");

        // 验签
        try {
            if (!StringRsaUtil.verify(body, request.getHeader("sign"), application.getPublicKey()))
                throw new BusinessException("验签失败");
        } catch (Exception e) {
//            throw new BusinessException("验签失败");
        }
        // TODO 鉴权 (暂时不需要)

        ApiMethodHandler.MethodAccessor methodAccessor = apiMethodHandler.getMethodAccessor(ssoRequest.getMethod());
        if (methodAccessor == null) throw new BusinessException("服务[" + ssoRequest.getMethod() + "]不存在");

        return methodAccessor.invoke(ApiMethodHandler.fastjsonArgument(body));
    }
}