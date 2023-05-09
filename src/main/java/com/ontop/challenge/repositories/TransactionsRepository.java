package com.ontop.challenge.repositories;

import com.ontop.challenge.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<Transactions, UUID> {
    List<Transactions> findByTransactionDateAndAmount(LocalDate transactionDate, int amount);
}
