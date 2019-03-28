package ee.icefire.vault.exception;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
public class VaultNotProperlyConfiguredException extends RuntimeException {

    public VaultNotProperlyConfiguredException() {
        super("Vault is not properly configured.");
    }
}