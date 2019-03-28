package ee.icefire.vault.resource;

import ee.icefire.vault.mapping.MappingSupport;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

public class VaultSecretResource extends ResourceSupport implements MappingSupport, Serializable {

    @NotNull
    private String data;

    @NotNull
    private String passphrase;

    @NotNull
    private Long userId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}