package com.ysl.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;

public class RSAUtil {
    private static final String RSA = "RSA";
    private static final int RSA_SIZE_2048 = 2048;
    private static final int RSA_SIZE_1024 = 1024;
    private static final String MD5_WITH_RSA = "MD5withRSA";
    private static final String SHA1_WITH_RSA = "SHA1WithRSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PUBLIC_KEY_B64 = "publicKeyBase64";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PRIVATE_KEY_B64 = "privateKeyBase64";

    public RSAUtil() {
    }

    public static Map<String, Object> create1024Key() throws NoSuchAlgorithmException {
        return createKey(1024);
    }

    public static Map<String, Object> create2048Key() throws NoSuchAlgorithmException {
        return createKey(2048);
    }

    public static Map<String, Object> createKey(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = null;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair key = keyGen.generateKeyPair();
        PublicKey pubKey = key.getPublic();
        PrivateKey priKey = key.getPrivate();
        Map<String, Object> map = new HashMap();
        map.put("publicKey", pubKey);
        map.put("privateKey", priKey);
        map.put("publicKeyBase64", Base64.encodeBase64String(pubKey.getEncoded()));
        map.put("privateKeyBase64", Base64.encodeBase64String(priKey.getEncoded()));
        return map;
    }

    public static void createKey(String publicFilePath, String privateFilePath, int keySize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        write(publicFilePath, pair.getPublic());
        write(privateFilePath, pair.getPrivate());
    }

    private static void write(String path, Object key) throws Exception {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            boolean creat = file.getParentFile().mkdirs();
            if (!creat) {
                System.out.println("创建文件目录异常！");
                return;
            }
        }

        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(key);
        } catch (Exception var12) {
            throw new Exception("密钥写入异常", var12);
        } finally {
            if (null != oos) {
                try {
                    oos.close();
                } catch (IOException var11) {
                    oos = null;
                }
            }

        }

    }

    public static boolean vertiy(byte[] data, byte[] sign, PublicKey pubk) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(pubk);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean vertiy(String data, String sign, PublicKey pubk) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        return vertiy(data.getBytes(), Base64.decodeBase64(sign), pubk);
    }

    public static byte[] sign(byte[] data, PrivateKey prik) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(prik);
        signature.update(data);
        return signature.sign();
    }

    public static String sign(String data, PrivateKey prik) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        return Base64.encodeBase64String(sign(data.getBytes(), prik)).trim();
    }

    public static PublicKey getPublicKey(String strPubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(strPubKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        return pubKey;
    }

    public static PrivateKey getPrivateKey(String strPriKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(strPriKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);
        return priKey;
    }

    public static PublicKey resolvePublicKey(String path) throws Exception {
        PublicKey pubkey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        PublicKey var4;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            pubkey = (PublicKey)ois.readObject();
            var4 = pubkey;
        } catch (Exception var8) {
            throw new Exception("解析异常", var8);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }

        return var4;
    }

    public static PrivateKey resolvePrivateKey(String path) throws Exception {
        PrivateKey prikey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        PrivateKey var4;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            prikey = (PrivateKey)ois.readObject();
            var4 = prikey;
        } catch (Exception var8) {
            throw new Exception("解析异常", var8);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }

        return var4;
    }

    public static String getBase64PublicKeyString(PublicKey pubKey) {
        return Base64.encodeBase64String(pubKey.getEncoded()).trim();
    }

    public static String getBase64PrivateKeyString(PrivateKey priKey) {
        return Base64.encodeBase64String(priKey.getEncoded()).trim();
    }

    public static String getBase64PublicKeyString(String path) throws Exception {
        PublicKey pubKey = resolvePublicKey(path);
        return getBase64PublicKeyString(pubKey);
    }

    public static String getBase64PrivateKeyString(String path) throws Exception {
        PrivateKey priKey = resolvePrivateKey(path);
        return getBase64PrivateKeyString(priKey);
    }

    public static String encrypt(Key key, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, key);
        byte[] miwen = cipher.doFinal(message.getBytes());
        return String.valueOf(new BigInteger(miwen));
    }

    public static String decrypt(Key key, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] miwen = (new BigInteger(message)).toByteArray();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, key);
        byte[] result = cipher.doFinal(miwen);
        return new String(result);
    }

    public static void main(String[] args) throws Exception {
        createKey("D:/RSA/public.key", "D:/RSA/private.key", 1024);
        PublicKey pubKey = resolvePublicKey("D:/RSA/public.key");
        PrivateKey priKey = resolvePrivateKey("D:/RSA/private.key");
        System.out.println("公钥：" + Base64.encodeBase64String(pubKey.getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64String(priKey.getEncoded()));
        String data = "B64DC35297E509D8078FDD64DDBBED73";
        String signData = sign(data, priKey);
        System.out.println("签名值为：" + signData);
        boolean result = vertiy(data, signData, pubKey);
        System.out.println("验签结果为：" + result);
    }
}
