package com.mruruc.service;

import com.mruruc.dto.RegistrationRequest;
import com.mruruc.exceptions.EntityNotFoundException;
import com.mruruc.model.User;
import com.mruruc.repository.RoleRepository;
import com.mruruc.repository.UserRepository;
import com.mruruc.security.user_adapter.UserAdapter;
import com.mruruc.validation.dtoValidator.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final DtoValidator<RegistrationRequest> dtoValidator;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DtoValidator<RegistrationRequest> dtoValidator, UserRepository repository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.dtoValidator = dtoValidator;
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findUserByEmail(username)
                .map(UserAdapter::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
    }

    public void saveUser(RegistrationRequest request) {
        // validate registration request ...
        dtoValidator.validate(request);
        // build user entity
        User user = buildUserEntity(request);
        // save user to database
        repository.save(user);
    }

    private User buildUserEntity(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                // to-do when the email integration must be updated!!!!!!!!!!!!!
                .isAccountEnabled(true)
                .isAccountLocked(false)
                .roles(Collections.singletonList(roleRepository.findRoleByRoleName("USER")
                        .orElseThrow(() -> new EntityNotFoundException("Role Not Found!"))))
                .build();
    }
}
