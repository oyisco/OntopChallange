package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class SourceRequest {
    private String type;
    private SourceInformationRequest sourceInformation;
    private AccountRequest account;
}
