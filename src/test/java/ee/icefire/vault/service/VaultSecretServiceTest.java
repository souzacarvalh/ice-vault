package ee.icefire.vault.service;

import ee.icefire.vault.crypto.RSAEncryptionServices;
import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultSecret;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultInvalidEncryptationException;
import ee.icefire.vault.exception.VaultSecretNotFoundException;
import ee.icefire.vault.repository.VaultSecretRepository;
import ee.icefire.vault.resource.VaultSecretResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Felipe Carvalho on 2019-04-01.
 */

@RunWith(MockitoJUnitRunner.class)
public class VaultSecretServiceTest {

    @Mock
    VaultSecretRepository vaultSecretRepositoryMock;

    @Mock
    RSAEncryptionServices rsaEncryptionServicesMock;

    @Mock
    VaultUserService vaultUserServiceMock;

    VaultSecretService vaultSecretService = new VaultSecretService();

    @Before
    public void setup() {
        vaultSecretService.setVaultUserService(vaultUserServiceMock);
        vaultSecretService.setRsaEncryptionServices(rsaEncryptionServicesMock);
        vaultSecretService.setVaultSecretRepository(vaultSecretRepositoryMock);
    }

    @Test
    public void whenGetExistingUserThenShouldReturnSuccessful() {
        when(vaultSecretRepositoryMock.findById(1L)).thenReturn(Optional.of(getSecretFixture()));
        assertThat(vaultSecretService.getSecretById(1L)).isNotNull();
    }

    @Test
    public void whenGetInvalidUserThenShouldThrowException() {
        when(vaultSecretRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> vaultSecretService.getSecretById(1L));

        assertThat(thrown).isInstanceOf(VaultSecretNotFoundException.class)
                .hasNoCause()
                .hasMessage("Vault Secret not found.");
    }

    @Test
    public void whenListExistingSecretsThenShouldReturnTheAvailableOnesForTheUser() {
        VaultUser vaultUser = getVaultUserFixture();
        when(vaultUserServiceMock.getUser(1L)).thenReturn(vaultUser);
        when(vaultSecretRepositoryMock.findByVaultUser(vaultUser)).thenReturn(getSecretListFixture());
        List<VaultSecretResource> secrets = vaultSecretService.listSecretsByUser(1L);
        assertThat(secrets).isNotNull();
        assertThat(secrets.size()).isEqualTo(3);
    }

    @Test
    public void whenSaveSecretThenShouldCallRepositoryAndSave() {
        VaultUser vaultUser = getVaultUserFixture();

        VaultKey userKey = new VaultKey();
        userKey.setPrivateKey("54hj5k4hjk4hj5k");
        userKey.setPublicKey("3hjk4hjk4hj5k4hj5k");
        vaultUser.setVaultKey(userKey);

        when(vaultUserServiceMock.getUser(anyLong())).thenReturn(vaultUser);
        when(rsaEncryptionServicesMock.getPrivateKey(anyString())).thenReturn(new DummyRSAPrivateKey());
        when(rsaEncryptionServicesMock.decrypt(any(byte[].class), any(PrivateKey.class))).thenReturn("Secret Revealed");
        when(rsaEncryptionServicesMock.encrypt(anyString(), any(PrivateKey.class))).thenReturn("asdfgh5fg4h5fgfg4");

        VaultSecretResource vaultSecretResource = new VaultSecretResource();
        vaultSecretResource.setPassphrase("65hggfh4g5fh4gf5==");

        vaultSecretService.save(vaultSecretResource);

        verify(vaultSecretRepositoryMock, times(1)).saveAndFlush(any(VaultSecret.class));
        verify(vaultSecretRepositoryMock, times(1)).findByVaultUser(any(VaultUser.class));
    }

    @Test
    public void whenSaveSecretAndGetEncryptionErrorsThenShouldThrowException() {
        VaultUser vaultUser = getVaultUserFixture();

        VaultKey userKey = new VaultKey();
        userKey.setPrivateKey("54hj5k4hjk4hj5k");
        userKey.setPublicKey("3hjk4hjk4hj5k4hj5k");
        vaultUser.setVaultKey(userKey);

        when(vaultUserServiceMock.getUser(anyLong())).thenReturn(vaultUser);
        when(rsaEncryptionServicesMock.getPrivateKey(anyString())).thenReturn(new DummyRSAPrivateKey());
        when(rsaEncryptionServicesMock.decrypt(any(byte[].class), any(PrivateKey.class))).thenThrow(new VaultInvalidEncryptationException());
        when(rsaEncryptionServicesMock.encrypt(anyString(), any(PrivateKey.class))).thenReturn("asdfgh5fg4h5fgfg4");

        VaultSecretResource vaultSecretResource = new VaultSecretResource();
        vaultSecretResource.setPassphrase("65hggfh4g5fh4gf5==");

        Throwable thrown = catchThrowable(() -> vaultSecretService.save(vaultSecretResource));

        assertThat(thrown).isInstanceOf(VaultInvalidEncryptationException.class)
                .hasNoCause()
                .hasMessage("Unable to encrypt/decrypt data the secret using the provided public/private key");
    }

    @Test
    public void whenDeleteAnExistingSecretThenShouldCallRepositoryAndDelete() {
        when(vaultSecretRepositoryMock.findById(1L)).thenReturn(Optional.of(getSecretFixture()));
        vaultSecretService.delete(1L);
        verify(vaultSecretRepositoryMock, times(1)).delete(any(VaultSecret.class));
    }

    @Test
    public void whenDeleteInvalidSecretThenShouldThrowException() {
        when(vaultSecretRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> vaultSecretService.delete(1L));

        assertThat(thrown).isInstanceOf(VaultSecretNotFoundException.class)
                .hasNoCause()
                .hasMessage("Vault Secret not found.");
    }

    private VaultSecret getSecretFixture() {
        VaultSecret vaultSecret = new VaultSecret();
        return vaultSecret;
    }

    private List<VaultSecret> getSecretListFixture() {
        List<VaultSecret> secrets = new ArrayList<>(3);
        secrets.add(new VaultSecret());
        secrets.add(new VaultSecret());
        secrets.add(new VaultSecret());
        return secrets;
    }

    private VaultUser getVaultUserFixture() {
        VaultUser vaultUser = new VaultUser();
        return vaultUser;
    }

    static class DummyRSAPrivateKey implements PrivateKey {

        @Override
        public String getAlgorithm() {
            return null;
        }

        @Override
        public String getFormat() {
            return null;
        }

        @Override
        public byte[] getEncoded() {
            return new byte[0];
        }
    }
}