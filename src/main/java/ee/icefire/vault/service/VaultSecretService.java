package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultSecret;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultInvalidEncryptationException;
import ee.icefire.vault.exception.VaultSecretNotFoundException;
import ee.icefire.vault.repository.VaultSecretRepository;
import ee.icefire.vault.resource.VaultSecretResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@Service
public class VaultSecretService {

    private VaultSecretRepository vaultSecretRepository;

    private VaultUserService vaultUserService;

    private RSAEncryptionServices rsaEncryptionServices;

    public VaultSecretResource getSecretById(final Long secretId) {
        Optional<VaultSecret> vaultSecretOpt = vaultSecretRepository.findById(secretId);
        VaultSecret vaultSecret = vaultSecretOpt.orElseThrow(VaultSecretNotFoundException::new);
        return vaultSecret.transform(VaultSecretResource.class);
    }

    public List<VaultSecretResource> listSecretsByUser(final Long userId) {
        VaultUser vaultUser = vaultUserService.getUser(userId);

        List<VaultSecret> secrets = vaultSecretRepository.findByVaultUser(vaultUser);
        return secrets.stream().map(secret -> secret.transform(VaultSecretResource.class))
                .collect(Collectors.toList());
    }


    public List<VaultSecretResource> save(final VaultSecretResource vaultSecretResource) {
        VaultUser vaultUser = vaultUserService.getUser(vaultSecretResource.getUserId());

        VaultSecret vaultSecret = vaultSecretResource.transform(VaultSecret.class);
        vaultSecret.setVaultUser(vaultUser);

        String encryptedPassphrase = encryptPassphraseWithPrivateKey(vaultUser.getVaultKey(), vaultSecretResource.getPassphrase());
        vaultSecret.setPassphrase(encryptedPassphrase);

        vaultSecretRepository.saveAndFlush(vaultSecret);

        return listSecretsByUser(vaultUser.getUserId());
    }

    /**
     * Decrypt the client AES passphrase (encrypted with the users RSA public key and Encoded in Base64)
     * using the user's RSA private for exchanging
     **/
    private String encryptPassphraseWithPrivateKey(final VaultKey vaultKey, final String encryptedPassphrase) {
        try {
            PrivateKey privateKey = rsaEncryptionServices.getPrivateKey(vaultKey.getPrivateKey());

            String passphrase = rsaEncryptionServices.decrypt(encryptedPassphrase.getBytes(), privateKey);

            return rsaEncryptionServices.encrypt(passphrase, privateKey);
        } catch (Exception e) {
            throw new VaultInvalidEncryptationException();
        }
    }

    public void delete(final Long secretId) {
        Optional<VaultSecret> vaultSecret = vaultSecretRepository.findById(secretId);
        vaultSecretRepository.delete(vaultSecret.orElseThrow(VaultSecretNotFoundException::new));
    }

    @Autowired
    public void setVaultSecretRepository(VaultSecretRepository vaultSecretRepository) {
        this.vaultSecretRepository = vaultSecretRepository;
    }

    @Autowired
    public void setVaultUserService(VaultUserService vaultUserService) {
        this.vaultUserService = vaultUserService;
    }

    @Autowired
    public void setRsaEncryptionServices(RSAEncryptionServices rsaEncryptionServices) {
        this.rsaEncryptionServices = rsaEncryptionServices;
    }
}