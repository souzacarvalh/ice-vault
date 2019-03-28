package ee.icefire.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VaultInvalidEncryptationException extends RuntimeException {

    public VaultInvalidEncryptationException() {
        super("Unable to encrypt/decrypt data the secret using the provided public/private key");
    }
}