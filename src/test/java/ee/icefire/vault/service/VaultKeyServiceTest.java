package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * Created by Felipe Carvalho on 2019-03-31.
 */

@RunWith(MockitoJUnitRunner.class)
public class VaultKeyServiceTest {

    @Mock
    RSAEncryptionServices rsaEncryptionServicesMock;

    VaultKeyService vaultKeyService = new VaultKeyService();

    @Before
    public void setup() {
        vaultKeyService.setRsaEncryptionServices(rsaEncryptionServicesMock);
    }

    @Test
    public void givenSystemIsConfiguredWhenGetKeyPairThenShouldReturn() throws NoSuchAlgorithmException {
        when(rsaEncryptionServicesMock.getKeyPair()).thenReturn(getKeyPairFixture());
        assertThat(vaultKeyService.generateVaultKey()).isNotNull();
        assertThat(vaultKeyService.generateVaultKey().getPrivateKey()).isNotEmpty();
        assertThat(vaultKeyService.generateVaultKey().getPublicKey()).isNotEmpty();
    }

    private KeyPair getKeyPairFixture() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        return generator.generateKeyPair();
    }
}