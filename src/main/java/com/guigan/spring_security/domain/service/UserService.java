package com.guigan.spring_security.domain.service;

import com.guigan.spring_security.domain.entities.User;
import com.guigan.spring_security.domain.repository.UserRepository;
import com.guigan.spring_security.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User create(User user) {
        var username = user.getUsername();
        var email = user.getEmail();
        var password = user.getPassword();

        if (username == null || username.isEmpty() || username.isBlank() ||
                email == null || email.isEmpty() || email.isBlank() ||
                password == null || password.isEmpty() || password.isBlank()) {
            throw new RuntimeException("User with invalid fields: null, empty or blank");
        }

        User userOptional = repository.getByEmail(user.getEmail());
        if (userOptional != null) {
            throw new RuntimeException("Records already found for this email: user with this email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return repository.create(user);
    }

    public User update(User user) {
        var entity = repository.getById(user.getId());

        if (entity == null)
            throw new RuntimeException("No records found for this ID: user does not exists");

        return repository.update(user);
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    public User getById(Long id) {
        var user = repository.getById(id);

        if (user == null)
            throw new RuntimeException("No records found for this ID: user does not exists");

        return user;
    }

    public void delete(Long id) {
        var user = repository.getById(id);

        if (user == null)
            throw new RuntimeException("No records found for this ID: user does not exists");

        repository.delete(id);
    }

    public AuthDto auth(AuthDto authDto) {
        User user = repository.getByEmail(authDto.getEmail());

        if (!this.encoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        StringBuilder password = new StringBuilder().append(user.getEmail()).append(":").append(user.getPassword());

        return AuthDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .token(Base64.getEncoder()
                        .withoutPadding()
                        .encodeToString(password
                                .toString()
                                .getBytes()))
                .build();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        // username = email
        Optional<User> userOptional = Optional.ofNullable(this.repository.getByEmail(username));

        return userOptional
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
