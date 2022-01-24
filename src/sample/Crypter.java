package sample;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


class Crypter{
    private static final String BLOWFISH = "Blowfish";
    String encrypt(String login, String password) throws Exception {
        byte[] keyData = (login).getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, BLOWFISH);
        Cipher cipher = Cipher.getInstance(BLOWFISH);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] result = cipher.doFinal(password.getBytes());
        return new BASE64Encoder().encode(result);
    }
    String decrypt(String login, String param) throws Exception {
        byte[] keyData = (login).getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, BLOWFISH);
        Cipher cipher = Cipher.getInstance(BLOWFISH);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] result = cipher.doFinal(new BASE64Decoder().decodeBuffer(param));
        return new String(result, StandardCharsets.UTF_8);
    }
}

