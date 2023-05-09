package com.ontop.challenge.service;

import com.ontop.challenge.dto.request.AccountCreationRequest;
import com.ontop.challenge.exception.ErrorResponse;
import com.ontop.challenge.mapstrut.AccountMapper;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Users;
import com.ontop.challenge.repositories.AccountsRepository;
import com.ontop.challenge.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountsRepository accountsRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Override
    public Accounts createAccount(AccountCreationRequest request) {
        Accounts returnAccount = accountsRepository.save(accountMapper.toEntity(request));
        Users users = new Users();
        users.setAccounts(returnAccount);
        users.setPassword(bcryptEncoder.encode("1234"));
        users.setUsername("idris");
        userRepository.save(users);
        return returnAccount;


    }

    public Accounts findByAccountNumber(String accountNumber) {
        Optional<Accounts> accounts = accountsRepository.findByAccountNumber(accountNumber);
        Accounts accounts1 = null;
        if (accounts.isPresent()) {
            accounts1 = accounts.get();
        }
        return accounts1;
    }


    public Accounts findAccount(Integer id) {
        Optional<Accounts> optionalAccounts = accountsRepository.findById(id);
        Accounts accounts1 = null;
        if (optionalAccounts.isPresent()) {
            accounts1 = optionalAccounts.get();
        }
        return accounts1;

    }

    public List<Accounts> getAllAccount() {
        return accountsRepository.findAll();
    }
}
