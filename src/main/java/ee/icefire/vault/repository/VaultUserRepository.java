package ee.icefire.vault.repository;

import ee.icefire.vault.entity.VaultUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */
public interface VaultUserRepository extends JpaRepository<VaultUser, Long> {

    VaultUser findByUsername(String username);

}