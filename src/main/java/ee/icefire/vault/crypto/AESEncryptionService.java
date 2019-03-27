package ee.icefire.vault.crypto;

import ee.icefire.vault.exception.EncryptationErrorException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@Service
public class AESEncryptionService {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    private static final String IV = "icefire$icefire$";

    public String encrypt(String data, String passphrase) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(passphrase.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with AES. Reason: %s", e.getMessage()));
        }
    }

    public String decrypt(String data, String passphrase) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(passphrase.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_CBC);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(data));
            return new String(decrypted);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with AES. Reason: %s", e.getMessage()));
        }
    }
}