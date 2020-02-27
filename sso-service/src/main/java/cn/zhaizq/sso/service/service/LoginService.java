package cn.zhaizq.sso.service.service;

import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggboy.framework.common.exception.BusinessException;
import com.ggboy.framework.utils.common.StringRsaUtil;
import com.ggboy.framework.utils.redis.RedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    private final static String key = "Login:Key";
    private final static String privateKey = "privateKey";
    private final static String publicKey = "publicKey";

    @Autowired
    private RedisWrapper redisWrapper;
    @Autowired
    private UserMapper userMapper;

    public User doLogin(String name, String password) throws NoSuchAlgorithmException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name", name);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null && user.getPassword().equals(getMd5(user.getId() + password + user.getId()))) {
            return user;
        }
        return null;
    }

    public User login(String name, String encryptedPassword) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String privateKey = getPrivateKeyByName(name);
        String password = StringRsaUtil.decryptByPrivateKey(encryptedPassword, privateKey);
        return doLogin(name, password);
    }

    public String getPublicKeyByName(String name) throws NoSuchAlgorithmException {
        Map<String, String> keyMap = redisWrapper.getJedis().hgetAll(key);

        if (keyMap == null || keyMap.isEmpty()) {
            StringRsaUtil.Keys keys = StringRsaUtil.genKeyPair();

            keyMap = new HashMap<>();
            keyMap.put(privateKey, keys.getPrivateKey());
            keyMap.put(publicKey, keys.getPublicKey());

            redisWrapper.getJedis().hset(key, keyMap);
            redisWrapper.expire(key, 300);
        }

        redisWrapper.set(buildBackupPrivateKey(name), keyMap.get(privateKey), 300);
        return keyMap.get(publicKey);
    }

    private String getPrivateKeyByName(String name) {
        return redisWrapper.get(buildBackupPrivateKey(name));
    }

    private String buildBackupPrivateKey(String name) {
        return "Login:UserPrivateKey:" + name;
    }

    public String getMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes());
        return new BigInteger(1, md5.digest()).toString(16);
    }
}