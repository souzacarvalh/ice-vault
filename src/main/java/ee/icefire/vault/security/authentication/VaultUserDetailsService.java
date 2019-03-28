package ee.icefire.vault.security.authentication;

import ee.icefire.vault.entity.VaultUser;
import ee.icefire.vault.repository.VaultUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class VaultUserDetailsService implements UserDetailsService {

    @Autowired
    private VaultUserRepository vaultUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        VaultUser user = vaultUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new VaultUserPrincipal(user);
    }
}