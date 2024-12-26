package com.mruruc;

import com.mruruc.security.model.Role;
import com.mruruc.security.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class TodoApp {

    public static void main(String[] args) {
        SpringApplication.run(TodoApp.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository repository) {
        return args -> {
            Optional<Role> userRole = repository.findRoleByRoleName("USER");
            if (userRole.isEmpty()) {
                repository.save(
                        Role.builder()
                                .roleName("USER")
                                .build()
                );
            }
        };
    }

}
