package ee.icefire.vault.resource;

import ee.icefire.vault.mapping.MappingSupport;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
public class VaultUserResource extends ResourceSupport implements MappingSupport, Serializable {

    @NotNull
    private String username;

    @Size.List(value = {@Size(min = 1, message = "not.empty"), @Size(max = 20, message = "exceed.max.length")})
    private String password;

    @NotNull
    @NotEmpty
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}