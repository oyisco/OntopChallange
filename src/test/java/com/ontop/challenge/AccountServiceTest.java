package com.ontop.challenge;

import com.ontop.challenge.dto.request.AccountCreationRequest;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.service.AccountService;
import com.ontop.challenge.service.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void createAccount(){

        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setAccountNumber("1885226711");
        accountCreationRequest.setBankName("TONY STAR");
        accountCreationRequest.setIdentityNumber("211927207");
        accountCreationRequest.setFirstName("Idris");
        accountCreationRequest.setSurname("Oyibo");

        Accounts accounts = new Accounts();
        accounts.setAccountNumber("1885226711");
        accounts.setBankName("TONY STAR");
        accounts.setIdentityNumber("211927207");
        accounts.setFirstName("Idris");
        accounts.setSurname("Oyibo");

      when(accountService.createAccount(accountCreationRequest)).thenReturn(accounts);

        Accounts saveAccounts = accountService.createAccount(accountCreationRequest);

        assertEquals(accountCreationRequest, saveAccounts);
    }

}
