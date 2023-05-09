package com.ontop.challenge.service;

import com.ontop.challenge.dto.request.*;
import com.ontop.challenge.dto.response.PaymentGatewayResponse;
import com.ontop.challenge.dto.response.TransactionResponse;
import com.ontop.challenge.dto.response.TransactionsHistory;
import com.ontop.challenge.enums.STATUS;
import com.ontop.challenge.exception.ErrorResponse;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Transactions;
import com.ontop.challenge.repositories.AccountsRepository;
import com.ontop.challenge.repositories.TransactionsRepository;

import com.ontop.challenge.webservice.WalletAPIService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    //you need to do dependency injection using constructor with all AllArgsConstructor
    private final AccountsRepository accountsRepository;
    private final WalletAPIService walletApiService;
    private final TransactionsRepository transactionsRepository;
    //get the onetop source information from application.properties because in the case i want it to be constant
    @Value("${source_type}")
    private String sourceType;
    @Value("${source_name}")
    private String sourceName;
    @Value("${source_accountNumber}")
    private String sourceAccountNumber;
    @Value("${currency}")
    private String currency;
    @Value("${source_routingNumber}")
    private String sourceRoutingNumber;

    public TransactionServiceImpl(AccountsRepository accountsRepository, WalletAPIService walletApiService, TransactionsRepository transactionsRepository) {
        this.accountsRepository = accountsRepository;
        this.walletApiService = walletApiService;
        this.transactionsRepository = transactionsRepository;
    }


    //in this method, the idea is to provide the url that once you want to
    // transfer from your wallet, it will take the information from the UI
    // (Recipients bank, routing number, account number etc.) and
    // send the request to the payment gateway url
    @Override
    public PaymentGatewayResponse transfer(TransferRequest request, Accounts accounts) {
        //this is to get records from the destination account using the USER ID
        //this is find the records from source using the userId and source id ;
        //if the above check is not empty the 10% of the amount will be calculated
        double fee = request.getAmount() * 0.1;
        int amountToTransfer = (int) (request.getAmount() - fee);
        //this line map a global variable to map the transaction/transfer between the Source which is th wallet and
        // the destination which is Recipients
        APIRequest apiRequest = new APIRequest();
        //set information for source  account
        //the information fo the source will always be the same
        SourceRequest sourceRequest = new SourceRequest();
        sourceRequest.setType(sourceType);
        SourceInformationRequest sourceInfo = new SourceInformationRequest();
        sourceInfo.setName(sourceName);
        sourceRequest.setSourceInformation(sourceInfo);
        //this set source account
        AccountRequest accountSourceRequest = new AccountRequest();
        accountSourceRequest.setAccountNumber(sourceAccountNumber);
        accountSourceRequest.setCurrency(currency);
        accountSourceRequest.setRoutingNumber(sourceRoutingNumber);
        sourceRequest.setAccount(accountSourceRequest);
        apiRequest.setSource(sourceRequest);

        //set information for destination account
        DestinationRequest destinationRequest = new DestinationRequest();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(accounts.getAccountNumber());
        accountRequest.setCurrency(currency);
        accountRequest.setRoutingNumber(accounts.getRoutingNumber());
        destinationRequest.setAccount(accountRequest);
        destinationRequest.setName(accounts.getFirstName() + " " + accounts.getSurname());
        apiRequest.setDestination(destinationRequest);
        apiRequest.setAmount(amountToTransfer);
        //send the information for wallet payment API
        PaymentGatewayResponse paymentGatewayResponse = walletApiService.createPayment(apiRequest);

        if (STATUS.Failed.equals(paymentGatewayResponse.getRequestInfo().getStatus())) {
            Transactions transactions = new Transactions();
            transactions.setAmount(paymentGatewayResponse.getPaymentInfo().getAmount());
            transactions.setStatus(paymentGatewayResponse.getRequestInfo().getError());
            transactions.setTransactionId(paymentGatewayResponse.getPaymentInfo().getId());
            transactions.setAccounts(accounts);
            this.transactionsRepository.save(transactions);
        } else {
            //this will return
            Transactions transactions = new Transactions();
            transactions.setAmount(paymentGatewayResponse.getPaymentInfo().getAmount());
            transactions.setStatus(paymentGatewayResponse.getRequestInfo().getStatus());
            transactions.setTransactionId(paymentGatewayResponse.getPaymentInfo().getId());
            transactions.setAccounts(accounts);
            this.transactionsRepository.save(transactions);
        }
        return paymentGatewayResponse;
    }

    //this deposit of the wallet
    @Override
    public ResponseEntity<TransactionResponse> topUpTransaction(TransactionRequest topUpTransaction) {
        return this.walletApiService.topUpTransaction(topUpTransaction);

    }

    //this is widral from the wallet by the user which has a charge of 10 on the amount using the user id%
    @Override
    public ResponseEntity<TransactionResponse> withDrawTransactions(TransactionRequest transactionRequest) {
        double fee = transactionRequest.getAmount() * 0.1;
        int amountToTransfer = (int) (transactionRequest.getAmount() - fee);
        TransactionRequest transactionRequest1 = new TransactionRequest();
        transactionRequest1.setAmount(amountToTransfer);
        transactionRequest1.setUser_id(transactionRequest.getUser_id());
        return this.walletApiService.withDrawTransactions(transactionRequest1);


    }

    public List<TransactionsHistory> findTransactionsByDateAndAmount(LocalDate transactionDate, int amount) {

        List<Transactions> transactions = this.transactionsRepository.findByTransactionDateAndAmount(transactionDate, amount);
        return transactions.stream()
                .map(// set other fields as needed
                        this::historyDto)
                .collect(Collectors.toList());

    }


    private TransactionsHistory historyDto(Transactions transactions1) {
        TransactionsHistory transactionsHistory = new TransactionsHistory();
        Accounts accounts = this.accountsRepository.getById(transactions1.getAccounts().getUserId());
        transactionsHistory.setAmount(transactions1.getAmount());
        transactionsHistory.setBankName(accounts.getBankName());
        transactionsHistory.setStatus(transactions1.getStatus());
        transactionsHistory.setTransactionDate(transactions1.getTransactionDate());
        // set other fields as needed
        return transactionsHistory;
    }
}
