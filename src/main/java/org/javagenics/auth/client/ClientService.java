package org.javagenics.auth.client;

import org.javagenics.auth.model.Client;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bPasswordEncoder;

    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.bPasswordEncoder = bPasswordEncoder;
    }

    public Client createClient(Client client) {
        Optional<ClientEntity> clientExists = this.clientRepository.findByEmailOrCpf(client.getEmail(), client.getCpf());
        if (clientExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email/Cpf already exists.");
        }

        // Create new database entity with encrypted pasword
        ClientEntity newClient = ClientEntity.builder().name(client.getName())
                .cpf(client.getCpf()).email(client.getEmail()).userType(client.getUserType()).build();
        newClient.setEncryptedPassword(bPasswordEncoder.encode(client.getPassword()));

        // DB write
        ClientEntity savedClient =  clientRepository.save(newClient);

        // Translate back to a response
        return Client.builder().name(savedClient.getName())
                .cpf(savedClient.getCpf()).email(savedClient.getEmail()).build();
    }
}
