package org.javagenics.auth.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    private String name;
    private String cpf;
    private String email;
    private String password;
    @Builder.Default
    private String userType = "client";
}
