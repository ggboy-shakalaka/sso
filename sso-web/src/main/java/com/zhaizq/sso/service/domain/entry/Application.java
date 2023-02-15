package com.zhaizq.sso.service.domain.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("basic_application")
public class Application {
    private Integer id;
    private String appId;
    private String appName;
    private String appDesc;
    private String publicKey;
    private Date createTime;

    public boolean isActive() {
        return true;
    }
}
