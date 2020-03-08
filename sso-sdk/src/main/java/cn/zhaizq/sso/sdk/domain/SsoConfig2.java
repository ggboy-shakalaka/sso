package cn.zhaizq.sso.sdk.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoConfig2 {
    private String server;
    private String appId;
    private boolean local;
    private SsoConfig server1;
}