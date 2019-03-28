package ee.icefire.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultApplication.class);
    }
}