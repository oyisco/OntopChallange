package com.ontop.challenge.repositories;

import com.ontop.challenge.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Integer> {
    Optional<Accounts> findByAccountNumber(String accountNumber);

}
