package ee.icefire.vault.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "EXCHANGE_PUBLIC_KEY", columnDefinition = "CLOB")
    private String exchangePublicKey;

    @Column(name = "EXCHANGE_PRIVATE_KEY", columnDefinition = "CLOB")
    private String exchangePrivateKey;

    @Column(name = "LAST_MODIFICATION_DATE", nullable = false)
    private LocalDateTime lastModDate;

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getExchangePublicKey() {
        return exchangePublicKey;
    }

    public void setExchangePublicKey(String exchangePublicKey) {
        this.exchangePublicKey = exchangePublicKey;
    }

    public String getExchangePrivateKey() {
        return exchangePrivateKey;
    }

    public void setExchangePrivateKey(String exchangePrivateKey) {
        this.exchangePrivateKey = exchangePrivateKey;
    }

    public LocalDateTime getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(LocalDateTime lastModDate) {
        this.lastModDate = lastModDate;
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