package be.alexandre01.dreamzon.network.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Logger;

public class Crypter {
    private SecretKey secretKey;
    private byte[] key;
    private Logger logger;
    public void Crypter(String key, Logger logger){
        this.logger = logger;
        MessageDigest sha = null;
        try {
            this.key = key.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            this.key = sha.digest(this.key);
            this.key = Arrays.copyOf(this.key,16);
            this.secretKey = new SecretKeySpec(this.key, "AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String value){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.warning("Erreur dans l'encryptage de la valeur");
        }
        return null;
    }

    public String decrypt(String value){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(value)));
        }catch (Exception e){
            logger.warning("Erreur dans le décryptage de la valeur");
        }
        return null;
    }
}
