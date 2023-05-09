package com.ontop.challenge.service;//package com.ontop.challenge.service;

import com.ontop.challenge.dto.request.AccountCreationRequest;
import com.ontop.challenge.dto.response.TransactionsHistory;
import com.ontop.challenge.model.Accounts;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;


public interface AccountService {
    //Response createAccount(AccountCreationRequest request);

    Accounts createAccount(AccountCreationRequest request);

    Accounts findAccount(Integer id);
    Accounts findByAccountNumber(String accountNumber);
  List< Accounts> getAllAccount();
}
