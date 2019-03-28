package ee.icefire.vault.security.rls;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Created by Felipe Carvalho on 2019-03-28.
 */

@Component
public class SpringSecurityRLSHandler implements RowLevelSecurityHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowLevelSecurityAspect.class);

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String VAULT_USER_TOKEN_KEY = "user-id";
    private static final String VAULT_USER_FILTER = "VAULT_USER_FILTER";
    private static final String VAULT_USER_ID_PARAM = "userId";

    @Lazy
    @Autowired
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void applyRowLevelSecurity() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            doApplyRowLevelSecurity(authentication);
        }
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void doApplyRowLevelSecurity(Authentication authentication) {
        if (!isVaultAdmin(authentication)) {
            Session session = entityManager.unwrap(Session.class);
            Long userId = getUserId(authentication);
            LOGGER.debug("Applying filter for {} and user id {}", authentication.getPrincipal(), userId);
            session.enableFilter(VAULT_USER_FILTER).setParameter(VAULT_USER_ID_PARAM, userId);
        }
    }

    private Long getUserId(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof OAuth2AuthenticationDetails) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(((OAuth2AuthenticationDetails) details).getTokenValue());
            return Long.parseLong(String.valueOf(accessToken.getAdditionalInformation().get(VAULT_USER_TOKEN_KEY)));
        }
        throw new RuntimeException("OAuth2AuthenticationDetails not found");
    }

    private boolean isVaultAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ADMIN_ROLE.equals(authority.getAuthority()));
    }
}