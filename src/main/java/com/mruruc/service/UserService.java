package com.mruruc.service;

import com.mruruc.dto.RegistrationRequest;
import com.mruruc.exceptions.EntityNotFoundException;
import com.mruruc.model.User;
import com.mruruc.security.repository.RoleRepository;
import com.mruruc.repository.UserRepository;
import com.mruruc.security.user_adapter.UserAdapter;
import com.mruruc.validation.dtoValidator.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final DtoValidator<RegistrationRequest> dtoValidator;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findUserByEmail(username)
                .map(UserAdapter::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
    }

    public void saveUser(RegistrationRequest request) {
        dtoValidator.validate(request);
        User user = buildUserEntity(request);
        repository.save(user);
    }

    private User buildUserEntity(RegistrationRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                // to-do when the email integration must be updated!!!!!!!!!!!!!
                .isAccountEnabled(true)
                .isAccountLocked(false)
                .roles(Collections.singletonList(roleRepository.findRoleByRoleName("USER")
                        .orElseThrow(() -> new EntityNotFoundException("Role Not Found!"))))
                .build();
    }
}
