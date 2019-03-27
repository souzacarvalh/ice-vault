package ee.icefire.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EncryptationErrorException extends RuntimeException {

    public EncryptationErrorException(String message) {
        super(message);
    }
}