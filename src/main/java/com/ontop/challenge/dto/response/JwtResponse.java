package com.ontop.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtResponse {
    private final String jwttoken;
    private String username;
    private UUID userId;

}
