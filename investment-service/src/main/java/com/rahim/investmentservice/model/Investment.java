package com.rahim.investmentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rgts.investments", indexes = {
        @Index(name = "idx_investments_account_gold", columnList = "account_id, gold_type_id"),
        @Index(name = "idx_investments_account_id", columnList = "account_id")
})
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_id", nullable = false)
    private Integer id;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

}