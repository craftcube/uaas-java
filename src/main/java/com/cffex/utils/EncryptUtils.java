package com.cffex.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


public class EncryptUtils {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "!", "#", "@", "a", "b", "c", "d", "*", "f", "g", "F" };
    public static String generateSalt(){
        return UUID.randomUUID().toString();
    }
    private static String byteArrayToHexString(byte[] b) {

        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / hexDigits.length;
        int d2 = n % hexDigits.length;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static byte[] digest(String rawPass, String salt){

        try {
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = msgDigest.digest(mergePasswordAndSalt(rawPass,salt).getBytes());
            return digest;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    private static String mergePasswordAndSalt(String password,String salt) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    public static String createCredential(String password,String salt){
        return byteArrayToHexString(digest(password,salt));
    }

    public static void main(String[] args){
        System.out.println(EncryptUtils.generateSalt());
        System.out.println(EncryptUtils.createCredential("chenchen",generateSalt()));
    }
}
