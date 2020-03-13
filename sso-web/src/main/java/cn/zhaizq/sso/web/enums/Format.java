package cn.zhaizq.sso.web.enums;

import com.ggboy.framework.common.exception.BusinessException;

public enum Format {
    XML("application/xml") {
        public <T> T parse(String data, Class<T> clazz) {
            throw new BusinessException("请求格式暂不支持");
        }

        public String format(Object object) {
            throw new BusinessException("请求格式暂不支持");
        }
    },
    JSON("application/json") {
        public <T> T parse(String data, Class<T> clazz) {
            return com.alibaba.fastjson.JSON.parseObject(data, clazz);
        }

        public String format(Object object) {
            return com.alibaba.fastjson.JSON.toJSONString(object);
        }
    }
    ;

    private String mimeType;
    Format(String mimeType) {
        this.mimeType = mimeType;
    }

    public static Format valueOof(String mimeType) {
        for (Format item : Format.values()) {
            if (item.mimeType.equals(mimeType))
                return item;
        }
        return null;
    }

    public abstract <T> T parse(String data, Class<T> clazz);
    public abstract String format(Object object);
}