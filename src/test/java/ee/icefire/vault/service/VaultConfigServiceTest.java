package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultConfig;
import ee.icefire.vault.exception.VaultNotProperlyConfiguredException;
import ee.icefire.vault.repository.VaultConfigRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Felipe Carvalho on 2019-03-31.
 */

@RunWith(MockitoJUnitRunner.class)
public class VaultConfigServiceTest {

    @Mock
    Environment envMock;

    @Mock
    VaultConfigRepository vaultConfigRepositoryMock;

    @Mock
    RSAEncryptionServices rsaEncryptionServicesMock;

    VaultConfigService vaultConfigService = new VaultConfigService();

    @Before
    public void setup() {
        vaultConfigService.setEnvironment(envMock);
        vaultConfigService.setRsaEncryptionServices(rsaEncryptionServicesMock);
        vaultConfigService.setVaultConfigRepository(vaultConfigRepositoryMock);
    }

    @Test
    public void whenGetSystemKeyPairThenShouldReturnFromDatabase() {
        when(vaultConfigRepositoryMock.findById(1L)).thenReturn(Optional.of(getVaultConfigFixture()));
        assertThat(vaultConfigService.getConfig()).isNotNull();
    }

    @Test
    public void whenConfigDoesNotExistThenShouldThrowException() {
        when(vaultConfigRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> vaultConfigService.getConfig());

        assertThat(thrown).isInstanceOf(VaultNotProperlyConfiguredException.class)
                .hasNoCause()
                .hasMessage("Vault is not properly configured.");
    }
    
    @Test
    public void givenSystemIsConfiguredWhenGetKeyPairThenShouldReturn() throws NoSuchAlgorithmException {
        when(vaultConfigRepositoryMock.findById(1L)).thenReturn(Optional.of(getVaultConfigFixture()));
        when(rsaEncryptionServicesMock.getKeyPair(anyString(), anyString())).thenReturn(getKeyPairFixture());
        assertThat(vaultConfigService.getSystemKeyPair()).isNotNull();
    }

    private VaultConfig getVaultConfigFixture() {
        VaultConfig vaultConfig = new VaultConfig();
        return vaultConfig;
    }

    private KeyPair getKeyPairFixture() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        return generator.generateKeyPair();
    }
}