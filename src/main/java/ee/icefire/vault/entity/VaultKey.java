package ee.icefire.vault.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@Entity
@Table(name = "VAULT_KEY")
public class VaultKey {

    @Id
    @Column(name = "KEY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyId;

    @Lob
    @Column(name = "PUBLIC_KEY")
    private String publicKey;

    @Lob
    @Column(name = "PRIVATE_KEY")
    private String privateKey;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private VaultUser vaultUser;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setVaultUser(VaultUser vaultUser) {
        this.vaultUser = vaultUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaultKey vaultKey = (VaultKey) o;
        return Objects.equals(keyId, vaultKey.keyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId);
    }
}