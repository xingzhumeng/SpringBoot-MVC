package com.example.util;


import com.example.exception.SaltInvalidateNumberException;
import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * 密码加盐工具类
 */
public class CredentialUtils {

    private static final Random random = new Random();

    /**
     * 随机生产16位数字类型的盐值
     *
     * @return 16位字符串
     */
    public static String randomSalt() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 20 2c b9 62 ac 59 075b964b07152d234b70
     * 将密码md5加密后，长度为32，再生成16个随机字符串,每两位中间插一个字符。
     * 形成了48位长度的字符串，在做md5加密。
     *
     * @param password  原密码
     * @param saltValue 盐值
     * @return 加盐加密后的密码
     */
    public static String cryptPassword(String password, String saltValue) {
        // 经过一次md5加密后的密码
        char[] pwdMd5 = DigestUtils.md5DigestAsHex(password.getBytes()).toCharArray();
        char[] saltCharArr = saltValue.toCharArray();

        if (saltCharArr.length != 16) {
            throw new SaltInvalidateNumberException("盐值长度必须为16");
        }

        char[] newPwdArray = new char[48];
        for (int i = 0; i < 16; i++) {
            // i:0; pwdMd5:0,1; saltCharArr:0; newPwdArray: 0,1,2
            // i:1; pwdMd5:2,3; saltCharArr:1; newPwdArray: 3,4,5
            // i:2; pwdMd5:4,5; saltCharArr:2; newPwdArray: 6,7,8
            newPwdArray[i * 3] = pwdMd5[i * 2]; // 0, 2, 4
            newPwdArray[i * 3 + 1] = pwdMd5[i * 2 + 1]; // 1, 3, 5
            newPwdArray[i * 3 + 2] = saltCharArr[i]; // 0, 1, 2
        }
        return DigestUtils.md5DigestAsHex(new String(newPwdArray).getBytes());
    }

    /**
     * 测试程序，可以用于生成盐/密码对，可以再次生成密码
     *
     * @param args
     */
    public static void main(String[] args) {
        String salt = randomSalt();
        salt = "5061638801537819";
        String password = cryptPassword("123456", salt);
        System.out.println(salt);
        System.out.println(password);
    }
}
