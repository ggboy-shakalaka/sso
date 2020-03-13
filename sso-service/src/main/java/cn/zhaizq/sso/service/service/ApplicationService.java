package cn.zhaizq.sso.service.service;

import cn.zhaizq.sso.service.domain.entry.Application;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    public Application query(String appId) {
        Application application = new Application();
        application.setAppId(appId);
        application.setAppName("HaHa");
        application.setPublicKey("");
        return application;
    }
}