package com.moin.transfer.entity;

import com.moin.transfer.common.enums.Currency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;

    private BigDecimal exchangeRate;

    private BigDecimal fee;

    private BigDecimal targetAmount;

    private LocalDateTime expireTime;

    private LocalDateTime requestTime;

    private boolean used;

    protected void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Quote(User user, BigDecimal amount, Currency targetCurrency, BigDecimal exchangeRate,
                 BigDecimal fee, BigDecimal targetAmount, LocalDateTime expireTime, boolean used, Currency currency) {
        this.user = user;
        if (user != null) {
            user.getQuotes().add(this);  // 양방향 관계 설정
        }
        this.amount = amount;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.fee = fee;
        this.targetAmount = targetAmount;
        this.expireTime = expireTime;
        this.used = used;
        this.currency = currency;
    }

    public void markAsUsed() {
        this.used = true;
        this.requestTime = LocalDateTime.now();
    }
}

