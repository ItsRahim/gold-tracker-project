package com.rahim.investmentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "holdings", schema = "rgts", indexes = {
        @Index(name = "idx_holdings_account_id", columnList = "account_id")
})
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('rgts.holdings_holding_id_seq'")
    @Column(name = "holding_id", nullable = false)
    private Integer id;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "investment_id", nullable = false)
    private Integer investmentId;

    @Column(name = "purchase_amount", precision = 15, scale = 2)
    private BigDecimal purchaseAmount;

    @Column(name = "current_value", precision = 15, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "profit_loss", precision = 15, scale = 2)
    private BigDecimal profitLoss;

    public Holding(Integer accountId, Integer investmentId) {
        this.accountId = accountId;
        this.investmentId = investmentId;
    }

    public Holding(Holding holding) {
        this.accountId = holding.getAccountId();
        this.investmentId = holding.getInvestmentId();
        this.purchaseAmount = holding.getPurchaseAmount();
        this.currentValue = holding.getCurrentValue();
        this.profitLoss = holding.getProfitLoss();
    }
}