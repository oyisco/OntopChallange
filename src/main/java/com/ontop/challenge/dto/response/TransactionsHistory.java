package com.ontop.challenge.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionsHistory {
    private String bankName;
    private String status;
    private LocalDate transactionDate;
    private int amount;


}
