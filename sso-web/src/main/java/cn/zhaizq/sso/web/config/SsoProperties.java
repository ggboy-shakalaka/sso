package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sso.server")
public class SsoProperties extends SsoConfig {
}