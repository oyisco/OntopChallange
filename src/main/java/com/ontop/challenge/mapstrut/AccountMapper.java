package com.ontop.challenge.mapstrut;

import com.ontop.challenge.dto.request.AccountCreationRequest;
import com.ontop.challenge.dto.response.AccountCreationResponse;
import com.ontop.challenge.model.Accounts;
import org.springframework.stereotype.Component;


@Component
public class AccountMapper {

    public AccountCreationResponse toDto(Accounts accounts) {
        AccountCreationResponse accountCreationResponse = new AccountCreationResponse();
        accountCreationResponse.setAccountNumber(accounts.getAccountNumber());
        accountCreationResponse.setBankName(accounts.getBankName());
        accountCreationResponse.setFirstName(accounts.getFirstName());
        accountCreationResponse.setSurname(accounts.getSurname());
        accountCreationResponse.setUserId(accounts.getUserId());
        accountCreationResponse.setIdentityNumber(accounts.getIdentityNumber());
        accountCreationResponse.setRoutingNumber(accounts.getRoutingNumber());
        return accountCreationResponse;


    }


    public Accounts toEntity(AccountCreationRequest accountCreationRequest) {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(accountCreationRequest.getAccountNumber());
        accounts.setBankName(accountCreationRequest.getBankName());
        accounts.setFirstName(accountCreationRequest.getFirstName());
        accounts.setSurname(accountCreationRequest.getSurname());
        accounts.setIdentityNumber(accountCreationRequest.getIdentityNumber());
        accounts.setRoutingNumber(accountCreationRequest.getRoutingNumber());
        return accounts;

    }
}
