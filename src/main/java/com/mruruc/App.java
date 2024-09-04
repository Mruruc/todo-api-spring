package com.mruruc;

import com.mruruc.model.Role;
import com.mruruc.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

   // @Bean
    public CommandLineRunner runner(RoleRepository repository) {
        return args -> {
            repository.save(
                    Role.builder()
                            .roleName("USER")
                            .build()
            );
        };
    }

}
