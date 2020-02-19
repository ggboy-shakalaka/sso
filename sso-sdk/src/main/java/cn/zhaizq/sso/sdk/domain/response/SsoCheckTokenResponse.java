package cn.zhaizq.sso.sdk.domain.response;

import cn.zhaizq.sso.sdk.domain.DataResponse;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoCheckTokenResponse extends DataResponse<SsoUser> {
}