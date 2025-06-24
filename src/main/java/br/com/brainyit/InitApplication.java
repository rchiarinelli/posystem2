package br.com.brainyit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "br.com.brainyit.posystem2.domain", "br.com.brainyit.posystem2.po" })
@EnableJpaRepositories(basePackages = { "br.com.brainyit" })
public class InitApplication {

    public static void main(final String[] args) {
        SpringApplication.run(InitApplication.class, args);
    }
}
