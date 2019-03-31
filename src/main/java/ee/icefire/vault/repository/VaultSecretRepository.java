package ee.icefire.vault.repository;

import ee.icefire.vault.entity.VaultSecret;
import ee.icefire.vault.entity.VaultUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */
public interface VaultSecretRepository extends JpaRepository<VaultSecret, Long> {

    List<VaultSecret> findByVaultUser(VaultUser vaultUser);

}