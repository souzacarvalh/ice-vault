package ee.icefire.vault.controller;

import ee.icefire.vault.resource.VaultUserResource;
import ee.icefire.vault.service.VaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */

@RestController
@RequestMapping("/api/user")
public class VaultUserController {

    @Autowired
    private VaultUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody VaultUserResource userResource) {
        userService.save(userResource);
    }

    @GetMapping
    public List<VaultUserResource> listUsers() {
        return userService.list();
    }

    @GetMapping("/{username}")
    public VaultUserResource getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.delete(username);
    }
}