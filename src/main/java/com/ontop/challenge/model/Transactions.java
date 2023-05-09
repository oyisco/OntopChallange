package com.ontop.challenge.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "transactions")
public class Transactions implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String transactionId;
    @Column(name = "status")
    private String status;
    @Column(name = "amount")
    private int amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Accounts accounts;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @PrePersist
    public void prePersist() {
        this.transactionDate = LocalDate.now();

    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDate.now();


    }

}
