package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class SsoBaseRequest {
    @Valid
    private SsoRequestHeader header;
}