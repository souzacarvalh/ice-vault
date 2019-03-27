package ee.icefire.vault.service;

import ee.icefire.vault.entity.VaultKey;
import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.exception.VaultUserNotFoundException;
import ee.icefire.vault.repository.VaultUserRepository;
import ee.icefire.vault.resource.VaultUserResource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private VaultUserRepository vaultUserRepository;

    @Autowired
    private VaultKeyService vaultKeyService;

    public VaultUserResource findByUserId(final Long userId) {
        Optional<VaultUser> vaultUserOpt = vaultUserRepository.findById(userId);
        VaultUser vaultUser = vaultUserOpt.orElseThrow(() -> new VaultUserNotFoundException("Vault user not found."));
        return vaultUser.transform(VaultUserResource.class);
    }

    public VaultUserResource findByUsername(final String username) {
        VaultUser vaultUser = Optional.ofNullable(vaultUserRepository.findByUsername(username))
                .orElseThrow(() -> new VaultUserNotFoundException("Vault user not found."));
        return vaultUser.transform(VaultUserResource.class);
    }

    public List<VaultUserResource> list() {
        List<VaultUser> licenses = vaultUserRepository.findAll();
        return licenses.stream().map(license -> license.transform(VaultUserResource.class))
                .collect(Collectors.toList());
    }

    public void save(final VaultUserResource userResource) {
        VaultUser vaultUser = userResource.transform(VaultUser.class);
        vaultUser.setCreatedDate(LocalDate.now());

        VaultKey keyPair = vaultKeyService.generateVaultKey();
        keyPair.setVaultUser(vaultUser);
        vaultUser.setVaultKey(keyPair);

        vaultUserRepository.save(vaultUser);
    }

    public void delete(final String username) {
        VaultUser vaultUser = Optional.ofNullable(vaultUserRepository.findByUsername(username))
                .orElseThrow(() -> new VaultUserNotFoundException("Vault user not found."));
        vaultUserRepository.delete(vaultUser);
    }
}