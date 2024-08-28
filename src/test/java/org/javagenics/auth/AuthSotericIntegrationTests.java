package org.javagenics.auth;

import io.github.danielliu1123.httpexchange.EnableExchangeClients;
import io.github.danielliu1123.httpexchange.RequestConfiguratorBeanPostProcessor;
import org.javagenics.auth.model.Client;
import org.javagenics.auth.model.Login;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableExchangeClients
@Testcontainers
class AuthSotericIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private RequestConfiguratorBeanPostProcessor requestConfiguratorBeanPostProcessor;

    @Test
    void signinRestTest() {
        // Given
        Client client = Client.builder().email("test@test.com")
                .password("password123").cpf("12345678901").name("bob").userType("client").build();

        // When
        ResponseEntity<Client> response = restTemplate.postForEntity("http://localhost:" + port + "/signin", client, Client.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(client.getEmail(), response.getBody().getEmail());
        assertEquals(client.getName(), response.getBody().getName());
        assertEquals(client.getCpf(), response.getBody().getCpf());
        assertNull(response.getBody().getPassword());

        // Given
        client = response.getBody();
        Login login = new Login();
        login.setEmail(client.getEmail());
        login.setPassword("password123");

        // When
        var tokenResponse = restTemplate.postForEntity("http://localhost:" + port + "/login", login, Map.class);
        var token = tokenResponse.getBody();

        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());
        assertNotNull(tokenResponse.getBody());
        assert token != null;
        assertEquals(1, token.size());
        assertTrue(token.containsKey("token"));
        assertNotNull(token.get("token"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenResponse.getBody().get("token"));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> finalResponse = restTemplate.exchange("http://localhost:" + port + "/protectedRoute",
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, finalResponse.getStatusCode());
    }
}
