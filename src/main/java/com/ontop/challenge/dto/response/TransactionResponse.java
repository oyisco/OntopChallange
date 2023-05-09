package com.ontop.challenge.dto.response;

import lombok.Data;

@Data
public class TransactionResponse {
    private Integer wallet_transaction_id;
    private Integer amount;
    private Integer user_id;
}
