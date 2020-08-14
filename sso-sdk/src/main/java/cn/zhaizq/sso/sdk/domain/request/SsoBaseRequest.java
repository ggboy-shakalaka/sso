package cn.zhaizq.sso.sdk.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class SsoBaseRequest {
    @Valid
    @NotNull
    private SsoRequestHeader header;
}