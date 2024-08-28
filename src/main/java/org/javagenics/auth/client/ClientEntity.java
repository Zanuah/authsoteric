package org.javagenics.auth.client;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long clientId;

    private String name;

    @Column(unique = true)
    private String cpf;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "encrypted_password")
    private String encryptedPassword;

    @Column(name = "user_type")
    @Builder.Default
    private String userType = "client";

    public Boolean isLoginCorrect(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.encryptedPassword);
    }
}
