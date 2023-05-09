package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class TransactionRequest {
    private Integer user_id;
    private Integer amount;

}
