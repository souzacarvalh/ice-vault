package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultConfig;
import ee.icefire.vault.exception.VaultNotProperlyConfiguredException;
import ee.icefire.vault.repository.VaultConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Optional;

/**
 * Created by Felipe Carvalho on 2019-03-28.
 */

@Service
public class VaultConfigService {

    @Autowired
    private Environment env;

    @Autowired
    private VaultConfigRepository vaultConfigRepository;

    @Autowired
    private RSAEncryptionServices rsaEncryptionServices;

    public Environment getEnv() {
        return env;
    }

    public VaultConfig getConfig() {
        Optional<VaultConfig> vaultConfigOpt = vaultConfigRepository.findById(1L);
        return vaultConfigOpt.orElseThrow(VaultNotProperlyConfiguredException::new);
    }

    public KeyPair getSystemKeyPair() {
        VaultConfig vaultConfig = getConfig();
        return rsaEncryptionServices.getKeyPair(vaultConfig.getExchangePublicKey()
                , vaultConfig.getExchangePrivateKey());
    }
}