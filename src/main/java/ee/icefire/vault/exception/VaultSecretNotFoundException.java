package ee.icefire.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VaultSecretNotFoundException extends RuntimeException {

    public VaultSecretNotFoundException() {
        super("Vault Secret not found.");
    }
}