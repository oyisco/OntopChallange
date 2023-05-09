package com.ontop.challenge.dto.response;

import lombok.Data;

@Data
public class TransactionsResponse {
    private Integer wallet_transaction_id;
    private Integer amount;
    private Integer user_id;
}
