package ee.icefire.vault.controller;

import ee.icefire.vault.resource.VaultSecretResource;
import ee.icefire.vault.service.VaultSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by Felipe Carvalho on 2019-03-25.
 */

@RestController
@RequestMapping("/api/secret")
@PreAuthorize("hasRole('ADMIN') or hasRole('ROLE_ENCRYPTOR')")
public class VaultSecretController {

    @Autowired
    private VaultSecretService secretDataService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSecret(@Valid @RequestBody VaultSecretResource secretDataResource) {
        secretDataService.save(secretDataResource);
    }

    @GetMapping("/{secretId}")
    public VaultSecretResource getSecret(@PathParam("secretId") Long secretId) {
        return secretDataService.getSecretById(secretId);
    }

    @GetMapping
    public List<VaultSecretResource> listSecrets(@RequestParam Long userId) {
        return secretDataService.listSecretsByUser(userId);
    }

    @DeleteMapping("/{secretId}")
    public void deleteSecret(@PathParam("secretId") Long secretId) {
        secretDataService.delete(secretId);
    }
}