package com.ontop.challenge.service;


import com.ontop.challenge.dto.request.TransactionRequest;
import com.ontop.challenge.dto.request.TransferRequest;
import com.ontop.challenge.dto.response.PaymentGatewayResponse;
import com.ontop.challenge.dto.response.TransactionResponse;
import com.ontop.challenge.dto.response.TransactionsHistory;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Transactions;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    PaymentGatewayResponse transfer(TransferRequest request, Accounts accounts);

    ResponseEntity<TransactionResponse> topUpTransaction(TransactionRequest topUpTransaction);

    ResponseEntity<TransactionResponse>  withDrawTransactions(TransactionRequest transactionRequest);


    List<TransactionsHistory> findTransactionsByDateAndAmount(LocalDate transactionDate, int amount);

}
