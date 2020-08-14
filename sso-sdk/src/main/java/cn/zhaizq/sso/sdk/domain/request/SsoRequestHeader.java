package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SsoRequestHeader {
    @NotEmpty
    private String request_id;
    @NotEmpty
    private String app_id;
    @NotEmpty
    private String method;
    @NotNull
    private Long timestamp;
}
