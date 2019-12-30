package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean ssoFilter() {
        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<SsoFilter>();
        filterRegistrationBean.setFilter(new SsoFilter());
        filterRegistrationBean.addInitParameter(SsoFilter.Conf.a, "");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
