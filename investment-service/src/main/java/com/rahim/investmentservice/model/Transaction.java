package com.rahim.investmentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rgts.transactions", indexes = {
        @Index(name = "idx_transactions_account_date", columnList = "account_id, transaction_date"),
        @Index(name = "idx_transactions_account_id", columnList = "account_id"),
        @Index(name = "idx_transactions_gold_type_id", columnList = "gold_type_id")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    @ColumnDefault("1")
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 4)
    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_price")
    private BigDecimal transactionPrice;

    @Column(name = "transaction_date")
    private OffsetDateTime transactionDate;

}