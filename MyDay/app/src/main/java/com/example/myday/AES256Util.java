package com.example.myday;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//public class AES256Util {
//
//    private static final int keySize = 128;
//    private static final int iterationCount = 10000;
//    private static String salt = "79752f1d3fd2432043c48e45b35b24645eb826a25c6f1804e9152665c345a552";
//    private static String iv = "2fad5a477d13ecda7f718fbd8a9f0443";
//    private static final String passPhrase = "passPhrase";
//
//    private final Cipher cipher;
//
//
//    public AES256Util() {
//        try {
//            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String encrypt(String plaintext) throws Exception {
//        return encrypt(salt, iv, passPhrase, plaintext);
//    }
//
//    public String decrypt(String ciphertext) throws Exception {
//        return decrypt(salt, iv, passPhrase, ciphertext);
//    }
//
//
//    private String encrypt(String salt, String iv, String passPhrase, String plaintext) throws Exception {
//        SecretKey key = generateKey(salt, passPhrase);
//        byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
//        return encodeBase64(encrypted);
//    }
//
//
//    private String decrypt(String salt, String iv, String passPhrase, String ciphertext) throws Exception {
//        SecretKey key = generateKey(salt, passPhrase);
//        byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, decodeBase64(ciphertext));
//        return new String(decrypted, "UTF-8");
//    }
//
//
//    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) throws Exception {
//        cipher.init(encryptMode, key, new IvParameterSpec(decodeHex(iv)));
//        return cipher.doFinal(bytes);
//    }
//
//
//    private SecretKey generateKey(String salt, String passPhrase) throws Exception {
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), decodeHex(salt), iterationCount, keySize);
//        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
//        return key;
//    }
//
//
//    private static String encodeBase64(byte[] bytes) {
//        return Base64.encodeBase64String(bytes);
//    }
//
//
//    private static byte[] decodeBase64(String str) {
//        return Base64.decodeBase64(str);
//    }
//
//
//    private static String encodeHex(byte[] bytes) {
//        return Hex.encodeHexString(bytes);
//    }
//
//
//    private static byte[] decodeHex(String str) throws Exception {
//        return Hex.decodeHex(str.toCharArray());
//    }
//
//
//    private static String getRandomHexString(int length) {
//        byte[] salt = new byte[length];
//        new SecureRandom().nextBytes(salt);
//        return encodeHex(salt);
//
//    }
//}

/**
* 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
*/
public class AES256Util {
   private String iv;
   private Key keySpec;
   /**
    * 16자리의 키값을 입력하여 객체를 생성한다.
    *
    * @param key
    *            암/복호화를 위한 키값
    * @throws UnsupportedEncodingException
    *             키값의 길이가 16이하일 경우 발생
    */
   final static String key = "비밀키입력하는곳";

   public AES256Util() throws UnsupportedEncodingException {
       this.iv = key.substring(0, 16);
       byte[] keyBytes = new byte[16];
       byte[] b = key.getBytes("UTF-8");
       int len = b.length;
       if (len > keyBytes.length) {
           len = keyBytes.length;
       }
       System.arraycopy(b, 0, keyBytes, 0, len);
       SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

       this.keySpec = keySpec;
   }

   /**
    /AES256 으로 암호화
    @param str
               암호화할 문자열
    @return
    @throws NoSuchAlgorithmException
    @throws GeneralSecurityException
    @throws UnsupportedEncodingException
    */
   public String encrypt(String str) throws NoSuchAlgorithmException,
           GeneralSecurityException, UnsupportedEncodingException {
       Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
       c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
       byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
       String enStr = new String(Base64.encodeBase64(encrypted));
       return enStr;
   }
   /**
    * AES256으로 암호화된 txt 를 복호화한다.
    * @param str
    *            복호화할 문자열
    * @return
    * @throws NoSuchAlgorithmException
    * @throws GeneralSecurityException
    * @throws UnsupportedEncodingException
    */
   public String decrypt(String str) throws NoSuchAlgorithmException,
           GeneralSecurityException, UnsupportedEncodingException {
       Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
       c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
       byte[] byteStr = Base64.decodeBase64(str.getBytes());
       return new String(c.doFinal(byteStr), "UTF-8");
   }
}
