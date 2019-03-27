package ee.icefire.vault.service;

import ee.icefire.vault.crypto.AESEncryptionService;
import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultUserNotFoundException;
import ee.icefire.vault.repository.VaultUserRepository;
import ee.icefire.vault.resource.SecretDataResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@Service
public class SecretDataService {

    @Autowired
    private VaultUserRepository vaultUserRepository;

    @Autowired
    private RSAEncryptionServices rsaEncryptionServices;

    @Autowired
    private AESEncryptionService aesEncryptionService;

    public void save(final SecretDataResource secretDataResource) {
        Optional<VaultUser> vaultUserOpt = vaultUserRepository.findById(secretDataResource.getUserId());
        VaultUser vaultUser = vaultUserOpt.orElseThrow(() -> new VaultUserNotFoundException("Vault user not found."));

        /*
         * Decrypt the client AES passphrase (encrypted with the users RSA public key and Encoded in Base64)
         * using the user's RSA private
         */
        VaultKey keyPair = vaultUser.getVaultKey();

        PrivateKey privateKey = rsaEncryptionServices.getPrivateKey(keyPair.getPrivateKey());

        String passphrase = rsaEncryptionServices.decrypt(secretDataResource.getPassphrase().getBytes(), privateKey);

        String plainText = aesEncryptionService.decrypt(secretDataResource.getData(), passphrase);

        System.out.println(plainText);
    }
}