package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class DestinationRequest {
    private String name;
    private AccountRequest account;
}
