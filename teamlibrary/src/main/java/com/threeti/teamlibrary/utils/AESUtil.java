package com.threeti.teamlibrary.utils;

import android.util.Base64;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by WangXY on 2016/1/12.15:08.
 */
public class AESUtil {
    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    public static final byte[] keys = new byte[]{0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c, 0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f, 0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09, 0x06, 0x07, 0x09, 0x0d};

    public static byte[] initkey() throws Exception {
        // 实例化密钥生成器
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM, "BC");
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(256);
        // kg.init(128);
        // 生成密钥
        SecretKey secretKey = kg.generateKey();
        // 获取二进制密钥编码形式
        return secretKey.getEncoded();
        // 为了便于测试，这里我把key写死了，如果大家需要自动生成，可用上面注释掉的代码
        // return new byte[] { 0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
        // 0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
        // 0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09,
        // 0x06, 0x07, 0x09, 0x0d };
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        // 实例化DES密钥
        // 生成密钥
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     *             密钥
     * @return byte[] 加密后的数据
     */
    public static byte[] encrypt(byte[] data) throws Exception {
        // 还原密钥
        Key k = toKey(keys);
        /**
         * 实例化 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     *             密钥
     * @return byte[] 解密后的数据
     */
    public static byte[] decrypt(byte[] data) throws Exception {
        // 欢迎密钥
        Key k = toKey(keys);
        /**
         * 实例化 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.decode(base64Code.getBytes(), Base64.DEFAULT);
    }

    /**
     * 解密算法
     *
     * @param encodeString
     * @return
     * @throws Exception
     */
    public static String getValue(String encodeString) throws Exception {
        byte[] data = AESUtil.decrypt(AESUtil.base64Decode(encodeString));
        return new String(data);
    }

}
