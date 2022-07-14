/**
 * (C) Copyright 2022 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.security;

import org.slf4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Arrays;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date: 20220626
 */
public class SecureData {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    private static final String DEFAULT_SECRET_KEY = "<([SecretKey!!To??Encrypt##Data@12345%6790])>";

    private static SecretKeyData createSecretKeySpec(String _secret, String _mdAlgo) {
        SecretKeySpec secretKey = null;
        SecretKeyData secretKeyData = null;
        byte[] key = null;
        try {
            key = Arrays.copyOf(
                    MessageDigest.getInstance(_mdAlgo).digest(_secret.getBytes("UTF-8")), 16);
            secretKey = new SecretKeySpec(key, Algorithms.AES);
            secretKeyData = new SecretKeyData(secretKey, key);
        } catch (NoSuchAlgorithmException|UnsupportedEncodingException  e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKeyData;
    }

    /**
     * Encrypt Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @return
     */
    public static String encrypt(String _data) {
        return encrypt(_data, DEFAULT_SECRET_KEY, Algorithms.SHA_1);
    }

    /**
     * Encrypt the String using AES/ECB/PKCS5Padding
     *
     * @param _data
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String encrypt(String _data, String _secret, String _algo) {
        return   encrypt(_data, Algorithms.AES_ECB_PKCS5Padding, _secret, _algo);
    }

    /**
     * Encrypt the String using AES/ECB/PKCS5Padding
     *
     * @param _data
     * @param _cipher
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String encrypt(String _data, String _cipher, String _secret, String _algo) {
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                if(_cipher != null && _cipher.contains("CBC")) {
                    IvParameterSpec ivSpec = new IvParameterSpec(secretKeyData.getKeyBytes());
                    cipher.init(Cipher.ENCRYPT_MODE, secretKeyData.getSecretKeySpec(),ivSpec);
                } else {
                    cipher.init(Cipher.ENCRYPT_MODE, secretKeyData.getSecretKeySpec());
                }
                return Base64.getEncoder().encodeToString(cipher.doFinal(_data.getBytes("UTF-8")));
            }
            log.info("SecretKeyData Generation Failed for Encryption.... ");
        } catch (Exception e) {
            log.info("Unable to Encrypt Data: " + e.toString());
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptData(String text, String authKy) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = authKy.getBytes("UTF-8");
        // System.out.println(b+","+b.length+","+keyBytes.length);
        int len = b.length;

        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        //BASE64Encoder encoder = new BASE64Encoder();
        //  return new String(Base64.encode(results, Base64.DEFAULT));
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
        //return encoder.encode(results);
    }

    /**
     * Decrypt Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @return
     */
    public static String decrypt(String _data) {
        return decrypt(_data, DEFAULT_SECRET_KEY, Algorithms.SHA_1);
    }

    /**
     * Decrypt the Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String decrypt(String _data, String _secret, String _algo) {
        return decrypt(_data, Algorithms.AES_CBC_PKCS5Padding, _secret,_algo);
    }

    /**
     * Decrypt the Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @param _cipher
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String decrypt(String _data, String _cipher, String _secret, String _algo) {
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                if(_cipher != null && _cipher.contains("CBC")) {
                    IvParameterSpec ivSpec = new IvParameterSpec(secretKeyData.getKeyBytes());
                    cipher.init(Cipher.DECRYPT_MODE, secretKeyData.getSecretKeySpec(),ivSpec);
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, secretKeyData.getSecretKeySpec());
                }
                return new String(cipher.doFinal(Base64.getDecoder().decode(_data)));
            }
            log.info("SecretKeyData Generation Failed for Decryption.... ");
        } catch (Exception e) {
            log.info("Unable to Decrypt the data: " + e.toString());
            e.printStackTrace();

        }
        return null;
    }

    public static String decryptData(String text, String authKy) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = authKy.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(Base64.getDecoder().decode(text));
        //BASE64Decoder decoder = new BASE64Decoder();
        return new String(results, "UTF-8");
    }

    public static String encryptData2(String text, String authKy, String _mdAlgo) throws Exception {
        byte[] key = Arrays.copyOf(MessageDigest
                .getInstance(_mdAlgo).digest(authKy.getBytes("UTF-8")), 16);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(key);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
    }

    public static String decryptData2(String text, String authKy, String _mdAlgo) throws Exception {
        byte[] key = Arrays.copyOf(MessageDigest
                .getInstance(_mdAlgo).digest(authKy.getBytes("UTF-8")), 16);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(key);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(Base64.getDecoder().decode(text));
        //BASE64Decoder decoder = new BASE64Decoder();
        return new String(results, "UTF-8");
    }

    /**
     * ONLY FOR TESTING PURPOSE
     * Code to Encrypt and Decrypt the Data
     * @param args
     */
    public static void main(String[] args) throws Exception{
        System.out.println("----------------------------------------------------------------------------------------");

        // test();
        test2();
        test3();

         for(int x=1; x<2; x++) {
            testEncryption(x);
         }
    }

    public static void test2() throws Exception  {
        String rawData  = "2580";
        String secret   = "GkO5Z8edREGjiBgC+WODSk/6WStxtwnrNq8XuAALgoI=";
        String cipher   = "AES/CBC/PKCS5Padding";
        String mdAlgo   = "SHA-256";
        System.out.println("Secret       : "+secret);
        System.out.println("Cipher       : "+cipher);
        System.out.println("MD Algorithm : "+mdAlgo);
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.println("Plain String : "+rawData);
        String rdEncrypt1 = "DG94t+omK+NUgrJIDdXTHw==";
        String rdDecrypt1 = decryptData("DG94t+omK+NUgrJIDdXTHw==", secret);
        System.out.println("Encrypted 1  : "+rdEncrypt1);
        System.out.println("Decrypted 1  : "+rdDecrypt1);

        String rdEncrypt2 = encryptData2(rawData, secret, mdAlgo);
        String rdDecrypt2 = decryptData2(rdEncrypt2, secret, mdAlgo);
        System.out.println("Encrypted 2  : "+rdEncrypt2);
        System.out.println("Decrypted 2  : "+rdDecrypt2);

        System.out.println("----------------------------------------------------------------------------------------");
    }

    public static void test3() {
        String rawData  = "2580";
        String secret   = "GkO5Z8edREGjiBgC+WODSk/6WStxtwnrNq8XuAALgoI=";
        String cipher   = Algorithms.AES_CBC_PKCS5Padding;
        String md_algo  = Algorithms.SHA_512;
        System.out.println("Secret       : "+secret);
        System.out.println("Cipher       : "+cipher);
        System.out.println("MD Algorithm : "+md_algo);
        System.out.println("----------------------------------------------------------------------------------------");
        String rdEncrypt = encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = decrypt(rdEncrypt, cipher, secret, md_algo);
        System.out.println("Plain String : "+rawData);
        System.out.println("Encrypted 3  : "+rdEncrypt);
        System.out.println("Decrypted 3  : "+rdDecrypt);
        System.out.println("----------------------------------------------------------------------------------------");
    }

    public static void test() throws Exception  {
        String rawData  = "2580";
        String secret   = "GkO5Z8edREGjiBgC+WODSk/6WStxtwnrNq8XuAALgoI=";

        String cipher   = Algorithms.AES_CBC_PKCS5Padding;
        String md_algo  = Algorithms.SHA_512;
        System.out.println("Secret       : "+secret);
        System.out.println("Cipher       : "+cipher);
        System.out.println("MD Algorithm : "+md_algo);
        System.out.println("----------------------------------------------------------------------------------------");
        String rdEncrypt1 = encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = decrypt(rdEncrypt1, cipher, secret, md_algo);
        System.out.println("Plain String : "+rawData);
        System.out.println("Encrypted 1  : "+rdEncrypt1);
        System.out.println("Decrypted 1  : "+rdDecrypt);
        String rdEncrypt2 = encryptData(rawData, secret);
        String rdDecrypt2 = decryptData(rdEncrypt2, secret);
        System.out.println("Encrypted 2  : "+rdEncrypt2);
        System.out.println("Decrypted 2  : "+rdDecrypt2);

        System.out.println("----------------------------------------------------------------------------------------");
    }


    /**
     * Test Encryption Decryption
     * @param _cnt
     */
    public static void testEncryption(int _cnt) {
        String rawData  = "My Name is Lincoln Hawk from the Galaxy Andromeda!";
        String secret   = "<([SecretKey!!To??Encrypt##Data@12345%6790])>-"+_cnt;
        String cipher   = Algorithms.AES_ECB_PKCS5Padding;
        String md_algo  = Algorithms.SHA_512;
        System.out.println("Secret       : "+secret);
        System.out.println("Cipher       : "+cipher);
        System.out.println("MD Algorithm : "+md_algo);
        System.out.println("----------------------------------------------------------------------------------------");
        String rdEncrypt = encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = decrypt(rdEncrypt, cipher, secret, md_algo);
        System.out.println("Plain String : "+rawData);
        System.out.println("Encrypted    : "+rdEncrypt);
        System.out.println("Decrypted    : "+rdDecrypt);
        System.out.println("----------------------------------------------------------------------------------------");

    }
}