package ee.icefire.vault.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Felipe Carvalho on 2019-03-28.
 */

@Entity
@Table(name = "VAULT_CONFIG")
public class VaultConfig {

    @Id
    @Column(name = "CONFIG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @Lob
    @Column(name = "EXCHANGE_PUBLIC_KEY")
    private String exchangePublicKey;

    @Lob
    @Column(name = "EXCHANGE_PRIVATE_KEY")
    private String exchangePrivateKey;

    @Column(name = "LAST_MODIFICATION_DATE", nullable = false)
    private LocalDateTime lastModDate;

    public String getExchangePublicKey() {
        return exchangePublicKey;
    }

    public String getExchangePrivateKey() {
        return exchangePrivateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaultConfig that = (VaultConfig) o;
        return Objects.equals(configId, that.configId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId);
    }
}