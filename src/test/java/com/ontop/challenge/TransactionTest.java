package com.ontop.challenge;

import com.ontop.challenge.dto.request.TransferRequest;
import com.ontop.challenge.dto.response.PaymentGatewayResponse;
import com.ontop.challenge.dto.response.PaymentInfo;
import com.ontop.challenge.dto.response.RequestInfo;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Transactions;
import com.ontop.challenge.repositories.AccountsRepository;
import com.ontop.challenge.service.TransactionService;
import com.ontop.challenge.service.TransactionServiceImpl;
import org.apache.catalina.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {
    @Mock
    AccountsRepository accountsRepository;
    @InjectMocks
    TransactionServiceImpl transactionService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // Mock the service method to return different DTOs based on the input
    @Test
    public void transfer() throws Exception {
        // Create a transferRequest object
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAccountNumber("1885226711");
        transferRequest.setAmount(3000);
        // Create a PaymentGatewayResponse object
        PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setId(String.valueOf(5));
        paymentInfo.setAmount(3000);
        paymentGatewayResponse.setPaymentInfo(paymentInfo);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setError("");
        requestInfo.setStatus("Processing");
        paymentGatewayResponse.setRequestInfo(requestInfo);
        // Mock the behavior of transactionService
        Optional<Accounts> accounts = accountsRepository.findByAccountNumber(transferRequest.getAccountNumber());
        when(transactionService.transfer(transferRequest,accounts.get())).thenReturn(paymentGatewayResponse);
        // Act
        PaymentGatewayResponse savePayment = transactionService.transfer(transferRequest,accounts.get());
        // Assert
        // Verify the result
        assertEquals(paymentGatewayResponse, savePayment);
        //  assertEquals(paymentGatewayResponse.getRequestInfo(), paymentGatewayResponse.getRequestInfo());


    }


}
