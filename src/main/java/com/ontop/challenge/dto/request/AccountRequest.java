package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String accountNumber;
    private String currency;
    private String routingNumber;
}
