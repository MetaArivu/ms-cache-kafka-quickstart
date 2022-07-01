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

    /**
     * Returns Message Digest Algorithm
     * @param _algo
     * @return
     */
    private static String getMessageDigestAlgo(int _algo) {
        Algorithms algos = new Algorithms();
        return algos.algos(_algo);
    }

    /**
     * Create the SecretKey Specs
     * @param _secret
     * @param _algo
     * @return
     */
    private static SecretKeyData createSecretKeySpec(String _secret, int _algo) {
        SecretKeySpec secretKey = null;
        SecretKeyData secretKeyData = null;
        byte[] key = null;
        try {
            key = Arrays.copyOf(
                    MessageDigest.getInstance(getMessageDigestAlgo(_algo))
                            .digest(_secret.getBytes("UTF-8")),
                    32);
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
    public static String encrypt(String _data, String _secret, int _algo) {
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
    public static String encrypt(String _data, String _cipher, String _secret, int _algo) {
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeyData.getSecretKeySpec());
                return Base64.getEncoder().encodeToString(cipher.doFinal(_data.getBytes("UTF-8")));
            }
            log.info("SecretKeyData Generation Failed for Encryption.... ");
        } catch (Exception e) {
            log.info("Unable to Encrypt Data: " + e.toString());
        }
        return "";
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
    public static String decrypt(String _data, String _secret, int _algo) {
        return decrypt(_data, Algorithms.AES_ECB_PKCS5Padding, _secret,_algo);
    }

    /**
     * Decrypt the Data using AES/ECB/PKCS5PADDING
     * @param _data
     * @param _cipher
     * @param _secret
     * @param _algo (MD5, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     */
    public static String decrypt(String _data, String _cipher, String _secret, int _algo) {
        try {
            SecretKeyData secretKeyData = createSecretKeySpec(_secret, _algo);
            if(secretKeyData != null) {
                Cipher cipher = Cipher.getInstance(_cipher);
                cipher.init(Cipher.DECRYPT_MODE, secretKeyData.getSecretKeySpec());
                return new String(cipher.doFinal(Base64.getDecoder().decode(_data)));
            }
            log.info("SecretKeyData Generation Failed for Decryption.... ");
        } catch (Exception e) {
            log.info("Unable to Decrypt the data: " + e.toString());
        }
        return null;
    }

    /**
     * ONLY FOR TESTING PURPOSE
     * Code to Encrypt and Decrypt the Data
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("----------------------------------------------------------------------------------------");
        for(int x=1; x<4; x++) {
            testEncryption(x);
        }
    }

    /**
     * Test Encryption Decryption
     * @param _cnt
     */
    public static void testEncryption(int _cnt) {
        String rawData  = "My Name is Lincoln Hawk from the Galaxy Andromeda!";
        String secret   = "<([SecretKey!!To??Encrypt##Data@12345%6790])>-"+_cnt;
        String cipher   = Algorithms.AES_ECB_PKCS5Padding;
        int md_algo     = Algorithms.SHA_512;
        System.out.println("Secret       : "+secret);
        System.out.println("Cipher       : "+cipher);
        System.out.println("MD Algorithm : "+new Algorithms().algos(md_algo));
        System.out.println("----------------------------------------------------------------------------------------");
        String rdEncrypt = encrypt(rawData, cipher, secret, md_algo);
        String rdDecrypt = decrypt(rdEncrypt, cipher, secret, md_algo);
        System.out.println("Plain String : "+rawData);
        System.out.println("Encrypted    : "+rdEncrypt);
        System.out.println("Decrypted    : "+rdDecrypt);
        System.out.println("----------------------------------------------------------------------------------------");

    }
}