package org.javagenics.auth;

import io.github.danielliu1123.httpexchange.EnableExchangeClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExchangeClients
public class AuthSotericApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSotericApplication.class, args);
    }
}
