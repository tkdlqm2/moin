package com.moin.transfer.repository;

import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT COALESCE(SUM(q.targetAmount), 0) FROM Quote q " +
            "WHERE q.user.id = :userId AND q.used = true " +
            "AND q.creationDtm >= :startOfDay AND q.creationDtm < :endOfDay")
    BigDecimal findDailyTotalAmountByUser(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Quote> findAllByUserAndUsedTrueAndCreationDtmBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Quote> findAllByUserAndUsedTrueOrderByCreationDtmDesc(User user);
}
