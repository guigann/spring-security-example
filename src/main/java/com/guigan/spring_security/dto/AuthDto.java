package com.guigan.spring_security.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    private Long id;
    private String email;
    private String password;
    private String token;

}
