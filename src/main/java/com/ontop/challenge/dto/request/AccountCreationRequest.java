package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class AccountCreationRequest {
    private String firstName;
    private String surname;
    private String routingNumber;
    private String identityNumber;
    private String accountNumber;
    private String bankName;
}
