package cn.zhaizq.sso.web.enums;

import com.ggboy.framework.common.exception.BusinessException;

public enum RequestFormat {
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
            try {
                return com.alibaba.fastjson.JSON.parseObject(data, clazz);
            } catch (Exception e) {
                throw new BusinessException("请求字段解析失败");
            }
        }

        public String format(Object object) {
            return com.alibaba.fastjson.JSON.toJSONString(object);
        }
    }
    ;

    private String mimeType;
    RequestFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public static RequestFormat valueOof(String mimeType) {
        if (mimeType == null)
            return null;

        for (RequestFormat item : RequestFormat.values()) {
            if (mimeType.startsWith(item.mimeType))
                return item;
        }
        return null;
    }

    public abstract <T> T parse(String data, Class<T> clazz);
    public abstract String format(Object object);
}