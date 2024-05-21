package com.rahim.investmentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions", schema = "rgts", indexes = {
        @Index(name = "idx_transactions_account_date", columnList = "account_id, transaction_date"),
        @Index(name = "idx_transactions_account_id", columnList = "account_id"),
        @Index(name = "idx_transactions_gold_type_id", columnList = "gold_type_id")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "gold_type_id", nullable = false)
    private Integer goldTypeId;

    @ColumnDefault("1")
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 4)
    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_price")
    private BigDecimal transactionPrice;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    public Transaction(Integer accountId, Integer goldTypeId, Integer quantity, String transactionType, BigDecimal transactionPrice, LocalDate transactionDate) {
        this.accountId = accountId;
        this.goldTypeId = goldTypeId;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.transactionPrice = transactionPrice;
        this.transactionDate = transactionDate;
    }
}