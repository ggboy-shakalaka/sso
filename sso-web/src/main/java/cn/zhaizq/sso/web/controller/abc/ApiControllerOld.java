package cn.zhaizq.sso.web.controller.abc;

import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import cn.zhaizq.sso.service.domain.entry.Application;
import cn.zhaizq.sso.service.service.ApplicationService;
import cn.zhaizq.sso.web.api.BaseApi;
import cn.zhaizq.sso.web.api.BaseApiParam;
import cn.zhaizq.sso.web.controller.BaseController;
import cn.zhaizq.sso.web.enums.Format;
import cn.zhaizq.sso.web.enums.SignType;
import cn.zhaizq.sso.web.utils.ValidateUtil;
import com.ggboy.framework.common.exception.BusinessException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiControllerOld extends BaseController {
    private final static int TIMESTAMP_TIME_OUT = 60 * 1000;

    @Autowired
    private Map<String, BaseApi<?>> apiMap;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public SsoResponse<?> action(@RequestBody(required = false) String body) {
        try {
            Object data = doAction(body);
            return new SsoResponse<>().code(200).data(data);

        } catch (BusinessException e) {
            return new SsoResponse<>().code(400).message(e.getMessage());

        } catch (Exception e) {
            log.error("系统内部错误", e);
            return new SsoResponse<>().code(500).message("系统内部错误");
        }
    }

    public Object doAction(String body) {
        String sign = request.getHeader("sign");
        String app_id = request.getHeader("app_id");
        String method = request.getHeader("method");
        String format = request.getHeader("format");
        String sign_type = request.getHeader("sign_type");
        String timestamp = request.getHeader("timestamp");

        try {
            if (System.currentTimeMillis() - Long.parseLong(timestamp) > TIMESTAMP_TIME_OUT)
                throw new BusinessException("请求已过期");
        } catch (Exception e) {
            throw new BusinessException("时间戳解析失败");
        }

        Application application = applicationService.query(app_id);
        if (application == null)
            throw new BusinessException("应用[" + app_id + "]未注册");

        // 验签
        SignType.valueOf(sign_type);
        try {
            SignType.valueOf(sign_type).verify(timestamp + body, sign, application.getPublicKey());
        }catch (Exception e) {
            throw new BusinessException("应用[" + app_id + "]未注册");
        }
        // TODO 鉴权 (暂时不需要)

        BaseApi<?> api = apiMap.get(method);
        if (api == null) {
            throw new BusinessException("服务[" + method + "]不存在");
        }

        try {
            BaseApiParam parse = Format.valueOf(format).parse(body, api.getParamClass());
            parse.setAppId(app_id);
            return api.service(parse);
        } catch (Exception e) {
            throw new BusinessException("");
        }
    }
}