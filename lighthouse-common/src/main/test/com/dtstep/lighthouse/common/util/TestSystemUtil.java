package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.List;

public class TestSystemUtil {

    @Test
    public void testGetMac() throws Exception {
        List<String> macList = SystemUtil.getMACAddress();
        System.out.println("macList is:" + JsonUtil.toJSONString(macList));
    }

    @Test
    public void testSecret() throws Exception {
        // 创建一个KeyGenerator实例，指定AES算法
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        // 初始化KeyGenerator，指定密钥长度（128, 192, 256位）
        keyGen.init(256);

        // 生成密钥
        SecretKey secretKey = keyGen.generateKey();

        // 将密钥编码为Base64字符串，便于存储和传输
        String encodedKey = Base64.encodeToString(secretKey.getEncoded());
        System.out.println("Generated AES Key: " + encodedKey);
    }
}
