package ee.icefire.vault.security.authorization;

import ee.icefire.vault.security.authentication.VaultUserPrincipal;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felipe Carvalho on 2019-03-28.
 */

@Component
public class VaultTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        VaultUserPrincipal vaultUser = (VaultUserPrincipal) authentication.getPrincipal();

        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("user_id", vaultUser.getId());
        additionalInfo.put("public_key", vaultUser.getPublicKey());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}