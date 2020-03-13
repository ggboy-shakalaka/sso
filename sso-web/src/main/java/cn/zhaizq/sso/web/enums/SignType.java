package cn.zhaizq.sso.web.enums;

import com.ggboy.framework.common.exception.BusinessException;
import com.ggboy.framework.utils.common.StringRsaUtil;

public enum SignType {
    MD5 {
        public void verify(String body, String sign, String key) {
            throw new BusinessException("验签方式暂不支持");
        }
    },
    RSA {
        public void verify(String body, String sign, String key) {
            try {
                StringRsaUtil.verify(body, sign, key);
            } catch (Exception e) {
                throw new BusinessException("验签失败");
            }
        }
    };

    public abstract void verify(String body, String sign, String key);

    public static SignType valueOof(String name) {
        for (SignType item : SignType.values()) {
            if (item.name().equals(name))
                return item;
        }
        return null;
    }
}
