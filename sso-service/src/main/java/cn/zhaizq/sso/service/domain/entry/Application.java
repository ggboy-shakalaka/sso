package cn.zhaizq.sso.service.domain.entry;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Application {
    private Integer id;
    private String appId;
    private String appName;
    private String appDesc;
    private String publicKey;
    private Date createTime;
}
