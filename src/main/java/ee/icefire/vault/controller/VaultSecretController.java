package ee.icefire.vault.controller;

import ee.icefire.vault.resource.SecretDataResource;
import ee.icefire.vault.service.SecretDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@RestController
@RequestMapping("/api/secret")
public class VaultSecretController {

    @Autowired
    private SecretDataService secretDataService;

    @PostMapping("/encrypt")
    public void encrypt(@Valid @RequestBody SecretDataResource secretDataResource) {
        secretDataService.save(secretDataResource);
    }

    @GetMapping("/decrypt")
    public SecretDataResource decrypt(@Valid @RequestBody SecretDataResource secretData) {
        return null;
    }

    @GetMapping
    public List<SecretDataResource> listSecrets(@RequestParam String userId) {
        return Collections.emptyList();
    }
}