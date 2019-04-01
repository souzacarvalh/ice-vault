package ee.icefire.vault.service;

import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultUserNotFoundException;
import ee.icefire.vault.repository.VaultUserRepository;
import ee.icefire.vault.resource.VaultUserResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Felipe Carvalho on 2019-04-01.
 */

@RunWith(MockitoJUnitRunner.class)
public class VaultUserServiceTest {

    @Mock
    VaultUserRepository vaultUserRepositoryMock;

    @Mock
    VaultKeyService vaultKeyServiceMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    VaultUserService vaultUserService = new VaultUserService();

    @Before
    public void setup() {
        vaultUserService.setVaultKeyService(vaultKeyServiceMock);
        vaultUserService.setVaultUserRepository(vaultUserRepositoryMock);
        vaultUserService.setPasswordEncoder(passwordEncoderMock);
    }

    @Test
    public void whenGetExistingUserThenShouldReturnSuccessful() {
        when(vaultUserRepositoryMock.findById(1L)).thenReturn(Optional.of(getVaultUserFixture()));
        assertThat(vaultUserService.getUser(1L)).isNotNull();
    }

    @Test
    public void whenGetInvalidUserThenShouldThrowException() {
        when(vaultUserRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> vaultUserService.getUser(1L));

        assertThat(thrown).isInstanceOf(VaultUserNotFoundException.class)
                .hasNoCause()
                .hasMessage("Vault user not found.");
    }

    @Test
    public void whenFindUserByIdThenShouldReturnSuccessful() {
        when(vaultUserRepositoryMock.findById(1L)).thenReturn(Optional.of(getVaultUserFixture()));
        assertThat(vaultUserService.findByUserId(1L)).isNotNull();
    }

    @Test
    public void whenFindInvalidUserByIdThenShouldThrowException() {
        when(vaultUserRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> vaultUserService.findByUserId(1L));

        assertThat(thrown).isInstanceOf(VaultUserNotFoundException.class)
                .hasNoCause()
                .hasMessage("Vault user not found.");
    }

    @Test
    public void whenFindUserByUsernameThenShouldReturnSuccessful() {
        when(vaultUserRepositoryMock.findByUsername(anyString())).thenReturn(getVaultUserFixture());
        assertThat(vaultUserService.findByUsername("JohnDoe")).isNotNull();
    }

    @Test
    public void whenFindInvalidUserByUsernameThenShouldThrowException() {
        when(vaultUserRepositoryMock.findByUsername(anyString())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> vaultUserService.findByUsername("JohnDoe"));

        assertThat(thrown).isInstanceOf(VaultUserNotFoundException.class)
                .hasNoCause()
                .hasMessage("Vault user not found.");
    }

    @Test
    public void whenListUsersThenShouldReturnAvailableUsers() {
        when(vaultUserRepositoryMock.findAll()).thenReturn(getVaultUserListFixture());
        List<VaultUserResource> users = vaultUserService.list();
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void whenThereAreNoAvailableUsersThenShouldReturnEmptyList() {
        when(vaultUserRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        List<VaultUserResource> users = vaultUserService.list();
        assertThat(users).isNotNull();
        assertThat(users.isEmpty()).isTrue();
    }

    @Test
    public void whenSaveUserThenShouldCallRepositoryAndSaveSuccessfully() {
        when(passwordEncoderMock.encode(anyString())).thenReturn("{bcrypt}s54da5s4daas4da54as");
        when(vaultKeyServiceMock.generateVaultKey()).thenReturn(new VaultKey());
        vaultUserService.save(new VaultUserResource());
        verify(vaultUserRepositoryMock, times(1)).save(any(VaultUser.class));
    }

    @Test
    public void whenDeleteUserThenShouldCallRepositoryAndDeleteSuccessfully() {
        when(vaultUserRepositoryMock.findByUsername("JohnDoe")).thenReturn(getVaultUserFixture());
        vaultUserService.delete("JohnDoe");
        verify(vaultUserRepositoryMock, times(1)).delete(any(VaultUser.class));
    }

    private VaultUser getVaultUserFixture() {
        VaultUser vaultUser = new VaultUser();
        return vaultUser;
    }

    private List<VaultUser> getVaultUserListFixture() {
        List<VaultUser> users = new ArrayList<>(3);
        users.add(new VaultUser());
        users.add(new VaultUser());
        users.add(new VaultUser());
        return users;
    }
}