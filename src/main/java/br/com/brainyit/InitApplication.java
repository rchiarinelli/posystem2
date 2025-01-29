package br.com.brainyit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @EnableJpaRepositories(basePackages = {"br.com.brainyit"})
// @EntityScan(basePackages = {"br.com.brainyit"})
public class InitApplication {

    public static void main(final String[] args) {
        SpringApplication.run(InitApplication.class);
    }

}
