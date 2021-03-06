package org.washcom.ex;

import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Joe Wolf
 */
public class Main {

    public static void main(String... args) throws Exception {
        for (Map.Entry<Object, Object> property : System.getProperties().entrySet()) {
            if (property.getKey().toString().startsWith("org.washcom.ex.")) {
                System.out.println(String.format("%1s=%2s", property.getKey(), property.getValue()));
            }
        }
        if (args.length == 0) {
            return;
        }

        String encryptionKey = "0123456789abcedf";
        if (args.length == 2) {
            encryptionKey = args[1];
        }
        String text = args[0];

        System.out.println("Text      : " + text);
        System.out.println("Key       : " + encryptionKey);
        byte[] encrypted = AESUtils.encrypt(text, encryptionKey);
        System.out.println("Encrypted : " + DatatypeConverter.printHexBinary(encrypted));
        System.out.println("Decrypted : " + AESUtils.decrypt(encrypted, encryptionKey));
    }

}

/**
 * Copied from
 * http://examples.javacodegeeks.com/core-java/security/invalidkeyexception/java-security-invalidkeyexception-how-to-solve-invalidkeyexception/
 */
class AESUtils {

    static final String INITIALIZATION_VECTOR = "AODVNUASDNVVAOVF";

    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}
