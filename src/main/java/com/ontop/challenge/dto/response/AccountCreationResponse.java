package com.ontop.challenge.dto.response;

import lombok.Data;

@Data
public class AccountCreationResponse {
    private Integer userId; // this will be the id of the user. let us assume this is just automatically available.
    private String firstName;
    private String surname;
    private String routingNumber;
    private String identityNumber;
    private String accountNumber;
    private String bankName;
}
