package com.guigan.spring_security.domain.repository;

import com.guigan.spring_security.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private static final List<User> users = new ArrayList<>();
    private static long nextId = 1;

    public List<User> getAll() {
        return users;
    }

    public User getById(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    public User getByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    public User create(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }

    public User update(User user) {
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                u.setUsername(user.getUsername());
                u.setEmail(user.getEmail());
                u.setPassword(user.getPassword());
                return u;
            }
        }
        return null;
    }

    public void delete(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
