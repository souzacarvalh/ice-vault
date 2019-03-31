package ee.icefire.vault.entity;

import ee.icefire.vault.mapping.MappingSupport;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@Entity
@Table(name = "VAULT_USER")
public class VaultUser implements MappingSupport {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDate createdDate;

    @Column(name = "ACCT_EXPIRED", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean accountExpired;

    @Column(name = "ACCT_LOCKED", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean accountLocked;

    @Column(name = "CREDENTIAL_EXPIRED", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean credentialExpired;

    @Column(name = "ENABLED", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "vaultUser", fetch = FetchType.LAZY)
    private VaultKey vaultKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "VAULT_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), foreignKey = @ForeignKey(name = "FK_USER_ROLES"))
    @Column(name = "VAULT_ROLE")
    @Enumerated(EnumType.STRING)
    private List<VaultRole> roles;

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public VaultKey getVaultKey() {
        return vaultKey;
    }

    public void setVaultKey(VaultKey vaultKey) {
        this.vaultKey = vaultKey;
    }

    public List<VaultRole> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaultUser vaultUser = (VaultUser) o;
        return Objects.equals(userId, vaultUser.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}