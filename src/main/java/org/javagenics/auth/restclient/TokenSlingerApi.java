package org.javagenics.auth.restclient;

import org.javagenics.auth.model.Client;
import org.javagenics.auth.model.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange("/")
public interface TokenSlingerApi {
    @PostExchange("/signin")
    public ResponseEntity<Client> createNewClient(@RequestBody Client newClient);

    @GetExchange("/protectedRoute")
    public ResponseEntity<String> protectedRoute(JwtAuthenticationToken token);

    @PostExchange("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login);
}
