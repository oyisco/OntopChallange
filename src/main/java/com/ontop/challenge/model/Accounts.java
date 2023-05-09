package com.ontop.challenge.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class Accounts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", updatable = false, nullable = false)
    private Integer userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "routing_number")
    private String routingNumber;
    @Column(name = "identity_number")
    private String identityNumber;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();

    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();


    }


}