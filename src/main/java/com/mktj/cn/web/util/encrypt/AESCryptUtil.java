package com.mktj.cn.web.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptUtil {
    //密件
    private static String sKey = "xiaoch0320170207";
    //加密向量
    private static String ivParameter = "20170207xiaoch03";

    /**
     * 加密功能
     *
     * @param sSrc
     * @return
     */
    public static String encrypt(String sSrc) {
        String result = "";
        try {
            //密件字节码
            byte[] raw = sKey.getBytes();
            //密件AES码
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            //加密向量（使用CBC模式，需要一个向量iv，可增加加密算法的强度）
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());

            //密文机初始化
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //(加密模式，加密字节，加密向量)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            //加密之后的字节码
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            // 此处使用BASE64做转码
            result = Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    /**
     * 解密功能
     *
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        try {
            //密件ASCII码
            byte[] raw = sKey.getBytes("ASCII");
            //密件AES码
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            //加密向量
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());

            //密码生成器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //(解密模式，加密字节，加密向量)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //先用BASE64反转码（加密的时候用BASE64转码）
            byte[] encrypted1 = Base64.decodeBase64(sSrc);
            //解密
            byte[] original = cipher.doFinal(encrypted1);
            //UTF-8编码
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String resetPassword() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        return "" + random;
    }

	/*
    public static void main(String[] args) {
		// 需要加密的字串
		String cSrc = "123456";
		System.out.println(cSrc + "  长度为" + cSrc.length());
		// 加密
		long lStart = System.currentTimeMillis();
		String enString = AESCryptUtil.encrypt(cSrc);
		System.out.println("加密后的字串是：" + enString + "长度为" + enString.length());

		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密耗时：" + lUseTime + "毫秒");
		// 解密
		lStart = System.currentTimeMillis();
		String DeString = AESCryptUtil.decrypt(enString);
		System.out.println("解密后的字串是：" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密耗时：" + lUseTime + "毫秒");
		
		String ps = resetPassword();
		System.out.println("随机生成密码："+ps);
	}
	*/

}
