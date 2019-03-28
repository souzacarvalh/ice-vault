package ee.icefire.vault.entity;

import ee.icefire.vault.mapping.MappingSupport;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@Entity
@Table(name = "VAULT_SECRET")
@FilterDef(name = "VAULT_USER_FILTER", parameters = @ParamDef(name = "userId", type = "long"))
@Filter(name = "VAULT_USER_FILTER", condition = "USER_ID = :userId")
public class VaultSecret implements MappingSupport {

    @Id
    @Column(name = "SECRET_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secretId;

    @Column(name = "DATA", columnDefinition = "CLOB")
    private String data;

    @Column(name = "PASSPHRASE", columnDefinition = "CLOB")
    private String passphrase;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private VaultUser vaultUser;

    public Long getSecretId() {
        return secretId;
    }

    public void setSecretId(Long secretId) {
        this.secretId = secretId;
    }

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

    public VaultUser getVaultUser() {
        return vaultUser;
    }

    public void setVaultUser(VaultUser vaultUser) {
        this.vaultUser = vaultUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaultSecret that = (VaultSecret) o;
        return Objects.equals(secretId, that.secretId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretId);
    }
}