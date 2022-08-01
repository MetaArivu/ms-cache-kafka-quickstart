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
import java.util.UUID;

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

    private static final String DEFAULT_SECRET_KEY  = "<([SecretKey!!To??Encrypt##Data@12345%6790])>";

    private static final String DEFAULT_ALGORITHM   = Algorithms.AES_CBC_PKCS5Padding;
    private static final String DEFAULT_MD_ALGO     = Algorithms.SHA_512;

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
     * Encrypt Data using AES/CBC/PKCS5PADDING
     * @param _data
     * @return
     */
    public static String encrypt(String _data) {
        return encrypt(_data, DEFAULT_SECRET_KEY, DEFAULT_MD_ALGO);
    }

    /**
     * Encrypt the String using AES/CBC/PKCS5PADDING
     *
     * @param _data
     * @param _secret
     * @return
     */
    public static String encrypt(String _data, String _secret) {
        return   encrypt(_data, DEFAULT_ALGORITHM, _secret, DEFAULT_MD_ALGO);
    }

    /**
     * Encrypt the String using AES/CBC/PKCS5PADDING
     *
     * @param _data
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String encrypt(String _data, String _secret, String _algo) {
        return   encrypt(_data, DEFAULT_ALGORITHM, _secret, _algo);
    }

    /**
     * Encrypt the String using AES/CBC/PKCS5PADDING
     *
     * @param _data
     * @param _cipher
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String encrypt(String _data, String _cipher, String _secret, String _algo) {
        if(_data == null) { return ""; }
        _cipher = (_cipher == null) ? DEFAULT_ALGORITHM: _cipher;
        _algo = (_algo == null) ? DEFAULT_MD_ALGO : _algo;
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                if(_cipher.contains("CBC")) {
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

    /**
     * Decrypt Data using AES/CBC/PKCS5PADDING
     * @param _data
     * @return
     */
    public static String decrypt(String _data) {
        return decrypt(_data, DEFAULT_SECRET_KEY, DEFAULT_MD_ALGO);
    }

    /**
     * Decrypt the Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @param _secret
     * @return
     */
    public static String decrypt(String _data, String _secret) {
        return decrypt(_data, DEFAULT_ALGORITHM, _secret,DEFAULT_MD_ALGO );
    }

    /**
     * Decrypt the Data using AES/CBC/PKCS5PADDING
     * @param _data
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String decrypt(String _data, String _secret, String _algo) {
        return decrypt(_data, DEFAULT_ALGORITHM, _secret,_algo);
    }

    /**
     * Decrypt the Data using AES/CBC/PKCS5PADDING
     * @param _data
     * @param _cipher
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String decrypt(String _data, String _cipher, String _secret, String _algo) {
        if(_data == null) { return ""; }
        _cipher = (_cipher == null) ? DEFAULT_ALGORITHM : _cipher;
        _algo = (_algo == null) ? DEFAULT_MD_ALGO : _algo;
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                if(_cipher.contains("CBC")) {
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

    /**
     * ONLY FOR TESTING PURPOSE
     * Code to Encrypt and Decrypt the Data
     * @param args
     */
    public static void main(String[] args) throws Exception{
        System.out.println("----------------------------------------------------------------------------------------");

        test0();
        test1();
        test2();

        for(int x=1; x<2; x++) {
            testEncryption(x);
        }
    }

    public static void test0() {
        String rawData  = "1234";
        String secret   = "eHEZ92vvd7jMqit6lkWa1sp7z6FpdVHRfRX8gZlslkw=";

        String rdEncrypt = SecureData.encrypt(rawData, secret);
        String rdDecrypt = SecureData.decrypt(rdEncrypt, secret);

        printResult(0, rawData,  secret, rdEncrypt,  rdDecrypt);
    }

    public static void test1() {
        String rawData  = "2580";
        String secret   = "GkO5Z8edREGjiBgC+WODSk/6WStxtwnrNq8XuAALgoI=";
        String cipher   = Algorithms.AES_CBC_PKCS5Padding;
        String md_algo  = Algorithms.SHA_512;

        String rdEncrypt = SecureData.encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = SecureData.decrypt(rdEncrypt, cipher, secret, md_algo);

        printResult(1, rawData,  secret,  cipher,  md_algo, rdEncrypt,  rdDecrypt);
    }

    public static void test2() {
        String rawData  = "2580";
        String secret   = "GkO5Z8edREGjiBgC+WODSk/6WStxtwnrNq8XuAALgoI=";
        String cipher   = Algorithms.AES_ECB_PKCS5Padding;
        String md_algo  = Algorithms.SHA_512;

        String rdEncrypt = SecureData.encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = SecureData.decrypt(rdEncrypt, cipher, secret, md_algo);

        printResult(2, rawData,  secret,  cipher,  md_algo, rdEncrypt,  rdDecrypt);
    }

    /**
     * Test Encryption Decryption
     * @param _cnt
     */
    public static void testEncryption(int _cnt) {
        String rawData = "My Name is Lincoln Hawk from the Galaxy Andromeda!";
        String secret = "<([SecretKey!!To??Encrypt##Data@12345%6790])>-" + _cnt;
        String cipher = Algorithms.AES_ECB_PKCS5Padding;
        String md_algo = Algorithms.SHA_512;

        String rdEncrypt = SecureData.encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = SecureData.decrypt(rdEncrypt, cipher, secret, md_algo);

        printResult(3, rawData, secret, cipher, md_algo, rdEncrypt, rdDecrypt);
    }

    /**
     * Print the Result with Default Algorithms
     * @param testNo
     * @param rawData
     * @param secret
     * @param rdEncrypt
     * @param rdDecrypt
     */
    public static void printResult(int testNo, String rawData, String secret, String rdEncrypt, String rdDecrypt) {
        printResult( testNo,  rawData,  secret,  DEFAULT_ALGORITHM,  DEFAULT_MD_ALGO, rdEncrypt,  rdDecrypt);
    }

    /**
     * Print the Result
     * @param testNo
     * @param rawData
     * @param secret
     * @param cipher
     * @param md_algo
     * @param rdEncrypt
     * @param rdDecrypt
     */
    public static void printResult(int testNo, String rawData, String secret, String cipher, String md_algo,
                                   String rdEncrypt, String rdDecrypt) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID         : "+uuid+" | Length = "+uuid.length());
        System.out.println("Secret Key   : "+secret);
        System.out.println("Cipher Suite : "+cipher);
        System.out.println("MD Algorithm : "+md_algo);
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Plain String : "+rawData);
        System.out.println("Encrypted "+testNo+"  : "+rdEncrypt);
        System.out.println("Decrypted "+testNo+"  : "+rdDecrypt);
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------------------------------");
    }
}