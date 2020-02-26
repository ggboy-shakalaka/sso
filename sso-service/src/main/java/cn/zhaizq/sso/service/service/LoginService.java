package cn.zhaizq.sso.service.service;

import com.ggboy.framework.utils.common.StringRsaUtil;
import com.ggboy.framework.utils.redis.RedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzh
 * @date 2020/2/25 15:11
 * @description
 */
@Service
public class LoginService {
    private final static String key = "Login:Key" ;
    private final static String privateKey = "privateKey";
    private final static String publicKey = "publicKey" ;
    @Autowired
    private RedisWrapper redisWrapper;


    public String getPublicKeyByUserName(String userName) throws NoSuchAlgorithmException {
        Map<String, String> keyMap = redisWrapper.getJedis().hgetAll(key);
        if (keyMap == null) {
            keyMap = geneKey();
            redisWrapper.getJedis().hset(key, keyMap);
            redisWrapper.expire(key, 300);
        }
        redisWrapper.set(build(userName), keyMap.get(privateKey), 300);
        return keyMap.get(publicKey);
    }

    public String getPrivateKeyByUserName(String userName) {
        return redisWrapper.get(build(userName));
    }

    public Map<String, String> geneKey() throws NoSuchAlgorithmException {
        StringRsaUtil.Keys keys = StringRsaUtil.genKeyPair();
        Map<String, String> map = new HashMap<>();
        map.put(privateKey, keys.getPrivateKey());
        map.put(publicKey, keys.getPublicKey());
        return map;
    }

    public String build(String name){
        return "Login:UserPrivateKey:"+name;
    }
}
