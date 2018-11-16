package me.ningsk.common.coder;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESCoder
{
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final byte[] ivBytes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private static SecretKeySpec generateKey(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = password.getBytes(CHARSET);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    public static String encrypt(String password, String message)
            throws GeneralSecurityException
    {
        try
        {
            SecretKeySpec key = generateKey(password);
            byte[] cipherText = encrypt(key, ivBytes, message.getBytes(CHARSET));

            return Base64.encodeToString(cipherText, 2);
        }
        catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] encrypt(SecretKeySpec key, byte[] iv, byte[] message)
            throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(1, key, ivSpec);
        byte[] cipherText = cipher.doFinal(message);

        return cipherText;
    }

    public static String decrypt(String password, String base64EncodedCipherText)
            throws GeneralSecurityException
    {
        try
        {
            SecretKeySpec key = generateKey(password);
            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, 2);
            byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);
            return new String(decryptedBytes, CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] decrypt(SecretKeySpec key, byte[] iv, byte[] decodedCipherText)
            throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(2, key, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(decodedCipherText);

        return decryptedBytes;
    }
}
