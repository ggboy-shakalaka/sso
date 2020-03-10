package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoFilter;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import com.alibaba.fastjson.JSON;
import com.ggboy.framework.common.exception.BusinessException;
import com.ggboy.framework.utils.redis.RedisWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

@Configuration
public class WebConfig {
    @Autowired
    private SsoConfig ssoConfig;

    @SneakyThrows
    @Bean
    public FilterRegistrationBean<SsoFilter> ssoFilter() throws IOException {
        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SsoFilter(ssoConfig));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }

    @Autowired
    private RedisProperties redisConfig;

    @Bean
    public RedisWrapper redisWrapper() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword(), redisConfig.getDatabase());
        return new RedisWrapper(jedisPool);
    }
}