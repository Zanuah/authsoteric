package org.javagenics.auth.client;

import org.javagenics.auth.model.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Client> createNewClient(@RequestBody Client newClient) {
        Client createdClient = this.clientService.createClient(newClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping("/protectedRoute")
    @PreAuthorize("hasAuthority('SCOPE_client')")
    public ResponseEntity<String> protectedRoute(JwtAuthenticationToken token) {
        return ResponseEntity.ok("Authorized");
    }
}
