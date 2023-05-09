package com.ontop.challenge.controller;

import com.ontop.challenge.dto.request.TransactionRequest;
import com.ontop.challenge.dto.request.TransferRequest;
import com.ontop.challenge.dto.response.PaymentGatewayResponse;
import com.ontop.challenge.dto.response.TransactionResponse;
import com.ontop.challenge.dto.response.TransactionsHistory;
import com.ontop.challenge.enums.STATUS;
import com.ontop.challenge.exception.ErrorResponse;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Transactions;
import com.ontop.challenge.repositories.AccountsRepository;
import com.ontop.challenge.service.TransactionService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionsController {
    private final TransactionService accountService;
    private final AccountsRepository accountsRepository;


    public TransactionsController(TransactionService accountService, AccountsRepository accountsRepository) {
        this.accountService = accountService;
        this.accountsRepository = accountsRepository;
    }


    @PostMapping("/transfer")
    public ResponseEntity<?> transferFunds(@RequestHeader("Authorization") String Authorization, @RequestBody TransferRequest request) {
        try {
            // validate request info and throw error.
            if (StringUtils.isBlank(request.getAccountNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "user account_id must not be null"));
            }

            if (request.getAmount() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "amount must not be null"));
            }
            Optional<Accounts> accounts = accountsRepository.findByAccountNumber(request.getAccountNumber());
            if (accounts.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "user not found"));
            }
            PaymentGatewayResponse paymentGatewayResponse = accountService.transfer(request, accounts.get());
            if (STATUS.Failed.equals(paymentGatewayResponse.getRequestInfo().getStatus())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(paymentGatewayResponse);
            }
            return ResponseEntity.ok().body(paymentGatewayResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", e.getMessage()));
        }


    }

    @PostMapping("/topUpTransaction")
    public ResponseEntity<?> topUpTransaction(@RequestHeader("Authorization") String Authorization, @RequestBody TransactionRequest topUpTransaction) {

        try {
            if (topUpTransaction.getUser_id() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "user_id must not be null"));
            }
            if (topUpTransaction.getAmount() <= 0) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "amount must not be null"));

            }
            ResponseEntity<TransactionResponse> responseEntity = accountService.topUpTransaction(topUpTransaction);
            if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "amount and user_id must not be null"));
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "user not found"));
            }
            return ResponseEntity.ok().body(responseEntity.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
        }


    }

    @PostMapping("/withDrawTransaction")
    public ResponseEntity<?> withDrawTransactions(@RequestHeader("Authorization") String Authorization, @RequestBody TransactionRequest withDrawTransactions) {

        try {
            if (withDrawTransactions.getUser_id() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "user_id must not be null"));
            }
            if (withDrawTransactions.getAmount() <= 0) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "amount must not be null"));

            }

            ResponseEntity<TransactionResponse> responseEntity = accountService.withDrawTransactions(withDrawTransactions);
            if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_BODY", "amount and user_id must not be null"));
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "user not found"));
            }
            return ResponseEntity.ok().body(responseEntity.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
        }


    }

    @GetMapping("/{amount}/{pageSize}/{pageNumber}")
    public ResponseEntity<?> findAccount(@RequestHeader("Authorization") String Authorization,
                                         @RequestParam(name = "transactionDate")
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
            ,
                                         @PathVariable(name = "amount") int amount, @PathVariable(name = "pageSize") int pageSize,
                                         @PathVariable(name = "pageNumber") int pageNumber) {
        try {

            int startIndex = (pageNumber - 1) * pageSize;
            List<TransactionsHistory> transactions = accountService.findTransactionsByDateAndAmount(transactionDate, amount);
            int endIndex = Math.min(startIndex + pageSize, transactions.size());

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "user not found"));
            }
            return ResponseEntity.ok().body(transactions.subList(startIndex, endIndex));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
        }
    }


}
