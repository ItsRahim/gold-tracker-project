package com.rahim.investmentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rgts.investments", indexes = {
        @Index(name = "idx_investments_account_id", columnList = "account_id")
})
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_id", nullable = false)
    private Integer id;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "total_purchase_amount")
    private BigDecimal totalPurchaseAmount;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    @Column(name = "profit_loss")
    private BigDecimal profitLoss;

    @Column(name = "total_weight")
    private BigDecimal totalWeight;

}