package org.javagenics.auth.token;

import java.time.Instant;

import org.javagenics.auth.client.ClientEntity;
import org.javagenics.auth.client.ClientRepository;
import org.javagenics.auth.model.Login;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenService {

    private final ClientRepository clientRepository;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.expiration.length}")
    private long jwtExpirationLength;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public TokenService(ClientRepository clientRepository, JwtEncoder jwtEncoder,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String login(Login login) {
        ClientEntity client = this.clientRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));

        boolean isPasswordCorrect = bCryptPasswordEncoder.matches(login.getPassword(), client.getEncryptedPassword());
        if (!isPasswordCorrect) {
            throw new BadCredentialsException("Email/password invalid");
        }

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .subject(client.getEmail())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpirationLength))
                .claim("scope", client.getUserType())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
