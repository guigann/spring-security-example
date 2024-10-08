package com.guigan.spring_security.web.controller;

import com.guigan.spring_security.domain.entities.User;
import com.guigan.spring_security.domain.service.UserService;
import com.guigan.spring_security.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    public final UserService service;

    @GetMapping({ "", "/" })
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<User> save(@RequestBody User user) {
        return new ResponseEntity<>(this.service.create(user), HttpStatus.CREATED);
//    return ResponseEntity.created(URI.create("/users/create")).body(this.service.create(user));
    }


    @PutMapping({ "", "/" })
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(this.service.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthDto> auth(@RequestBody AuthDto user) {
        return ResponseEntity.ok(this.service.auth(user));
    }

}
