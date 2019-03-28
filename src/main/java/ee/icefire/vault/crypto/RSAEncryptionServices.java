package ee.icefire.vault.crypto;

import ee.icefire.vault.exception.EncryptationErrorException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@Service
public class RSAEncryptionServices {

    private static final int RSA_LENGTH = 2048;
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_ALGORITHM_PKCS = "RSA/ECB/PKCS1Padding";

    public KeyPair getKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            generator.initialize(RSA_LENGTH);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptationErrorException(String.format("Could not generate a new RSA KeyPair. Reason: %s", e.getMessage()));
        }
    }

    public KeyPair getKeyPair(String base64PublicKey, String base64PrivateKey) {
        return new KeyPair(getPublicKey(base64PublicKey), getPrivateKey(base64PrivateKey));
    }

    public String encrypt(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_PKCS);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }

    public String encrypt(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_PKCS);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }

    public String decrypt(byte[] base64Data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_PKCS);
            cipher.init(Cipher.PRIVATE_KEY, publicKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(base64Data)));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }

    public String decrypt(byte[] base64Data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_PKCS);
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(base64Data)));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }

    public PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }

    public PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptationErrorException(String.format("Could not encrypt/decrypt data with RSA. Reason: %s", e.getMessage()));
        }
    }
}