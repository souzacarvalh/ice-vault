package ee.icefire.vault.service;

import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultUserNotFoundException;
import ee.icefire.vault.repository.VaultUserRepository;
import ee.icefire.vault.resource.VaultUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@Service
public class VaultUserService {

    private VaultUserRepository vaultUserRepository;

    private VaultKeyService vaultKeyService;

    private PasswordEncoder passwordEncoder;

    public VaultUser getUser(Long userId) {
        Optional<VaultUser> vaultUserOpt = vaultUserRepository.findById(userId);
        VaultUser vaultUser = vaultUserOpt.orElseThrow(VaultUserNotFoundException::new);
        return vaultUser;
    }

    public VaultUserResource findByUserId(final Long userId) {
        Optional<VaultUser> vaultUserOpt = vaultUserRepository.findById(userId);
        VaultUser vaultUser = vaultUserOpt.orElseThrow(VaultUserNotFoundException::new);
        return vaultUser.transform(VaultUserResource.class);
    }

    public VaultUserResource findByUsername(final String username) {
        VaultUser vaultUser = Optional.ofNullable(vaultUserRepository.findByUsername(username))
                .orElseThrow(VaultUserNotFoundException::new);
        return vaultUser.transform(VaultUserResource.class);
    }

    public List<VaultUserResource> list() {
        List<VaultUser> users = vaultUserRepository.findAll();
        return users.stream().map(user -> user.transform(VaultUserResource.class))
                .collect(Collectors.toList());
    }

    public void save(final VaultUserResource userResource) {
        VaultUser vaultUser = userResource.transform(VaultUser.class);
        vaultUser.setCreatedDate(LocalDate.now());
        vaultUser.setPassword(passwordEncoder.encode(userResource.getPassword()));
        vaultUser.setEnabled(true);

        VaultKey keyPair = vaultKeyService.generateVaultKey();
        keyPair.setVaultUser(vaultUser);
        vaultUser.setVaultKey(keyPair);

        vaultUserRepository.save(vaultUser);
    }

    public void delete(final String username) {
        VaultUser vaultUser = Optional.ofNullable(vaultUserRepository.findByUsername(username))
                .orElseThrow(VaultUserNotFoundException::new);
        vaultUserRepository.delete(vaultUser);
    }

    @Autowired
    public void setVaultUserRepository(VaultUserRepository vaultUserRepository) {
        this.vaultUserRepository = vaultUserRepository;
    }

    @Autowired
    public void setVaultKeyService(VaultKeyService vaultKeyService) {
        this.vaultKeyService = vaultKeyService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}