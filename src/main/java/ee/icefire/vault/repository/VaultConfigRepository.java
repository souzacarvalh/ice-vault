package ee.icefire.vault.repository;

import ee.icefire.vault.entity.VaultConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */
public interface VaultConfigRepository extends JpaRepository<VaultConfig, Long> {

}