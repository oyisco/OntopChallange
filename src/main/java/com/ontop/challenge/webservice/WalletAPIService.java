package com.ontop.challenge.webservice;//package com.ontop.challenge.webservice;

import com.google.gson.Gson;
import com.ontop.challenge.dto.request.APIRequest;
import com.ontop.challenge.dto.request.TransactionRequest;
import com.ontop.challenge.dto.response.PaymentGatewayResponse;
import com.ontop.challenge.dto.response.TransactionResponse;
import com.ontop.challenge.exception.ErrorResponse;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Transactions;
import com.ontop.challenge.repositories.AccountsRepository;
import com.ontop.challenge.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.Optional;

@Component
public class WalletAPIService {


    @Value("${wallet_transfer_url}")
    private String walletTransferURL;


    @Value("${wallet_transaction_url}")
    private String walletTransactionUrl;

    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;


    private RestTemplate restTemplate = new RestTemplate();

    public WalletAPIService(TransactionsRepository transactionsRepository, AccountsRepository accountsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.accountsRepository = accountsRepository;
    }

    private HttpHeaders paymentHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
        return headers;
    }


    public PaymentGatewayResponse createPayment(APIRequest apiRequest) {
        Gson gson = new Gson();
        HttpEntity<String> request = new HttpEntity<>(gson.toJson(apiRequest), paymentHeader());
        return restTemplate.postForObject(walletTransferURL, request, PaymentGatewayResponse.class);

    }


    public ResponseEntity<TransactionResponse> topUpTransaction(TransactionRequest topUpTransaction) {


        ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(walletTransactionUrl, topUpTransaction, TransactionResponse.class);
        //this will update transactions using getWallet_transaction_id but it will not update in a real sense because the API generate different id.
        if (response.getStatusCode() == HttpStatus.OK) {
            Transactions transactions = new Transactions();
            transactions.setAmount(Objects.requireNonNull(response.getBody()).getAmount());
            transactions.setStatus("transaction updated successfully");
            transactions.setTransactionId(String.valueOf(response.getBody().getWallet_transaction_id()));
            Accounts accounts = this.findAccount(topUpTransaction.getUser_id());
            transactions.setAccounts(accounts);
            transactionsRepository.save(transactions);
        }
        return response;

    }


    public ResponseEntity<TransactionResponse> withDrawTransactions(TransactionRequest withDrawTransactions) {

        ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(walletTransactionUrl, withDrawTransactions, TransactionResponse.class);
        //this will update transactions using getWallet_transaction_id but it will not update in a real sense because the API generate different id.
        if (response.getStatusCode() == HttpStatus.OK) {
            Transactions transactions = new Transactions();
            transactions.setAmount(Objects.requireNonNull(response.getBody()).getAmount());
            transactions.setStatus("transaction completed");
            transactions.setTransactionId(String.valueOf(response.getBody().getWallet_transaction_id()));
            Accounts accounts = this.findAccount(withDrawTransactions.getUser_id());
            transactions.setAccounts(accounts);
            transactionsRepository.save(transactions);
        }
        return response;


    }

    public void insertOrUpdateTransactionHistory(Transactions transactions) {
        try {
            this.transactionsRepository.save(transactions);

        } catch (Exception e) {
            //return Response.status(500).entity(new ErrorResponse(500, "GENERIC_ERROR", "something bad happened")).build();
            throw new RuntimeException("something bad happened");
        }
    }

    public Accounts findAccount(Integer id) {
        Optional<Accounts> optionalAccounts = accountsRepository.findById(id);
        if (optionalAccounts.isPresent()) {
            return optionalAccounts.get();
        } else {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }

}
