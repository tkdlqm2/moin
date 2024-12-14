package com.moin.transfer.repository;

import com.moin.transfer.dto.response.QuoteAmount;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT new com.moin.transfer.dto.response.QuoteAmount(q.targetAmount, q.targetCurrency, q.amount) " +
            "FROM Quote q " +
            "WHERE q.user.id = :userId AND q.used = true " +
            "AND q.creationDtm >= :startOfDay AND q.creationDtm < :endOfDay")
    List<QuoteAmount> findDailyQuoteAmountsByUser(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Quote> findAllByUserAndUsedTrueAndCreationDtmBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Quote> findAllByUserAndUsedTrueOrderByCreationDtmDesc(User user);
}
