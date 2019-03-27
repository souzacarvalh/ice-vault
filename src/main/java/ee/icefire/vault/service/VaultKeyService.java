package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Base64;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@Service
public class VaultKeyService {

    @Autowired
    private RSAEncryptionServices rsaEncryptionServices;

    public VaultKey generateVaultKey() {
            KeyPair keyPair = rsaEncryptionServices.getKeyPair();

            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            VaultKey vaultKey = new VaultKey();
            vaultKey.setPublicKey(publicKey);
            vaultKey.setPrivateKey(privateKey);

            return vaultKey;
    }
}