package org.foodie_tour.modules.transaction.repository;

import org.foodie_tour.modules.transaction.entity.Transactions;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0L) FROM Transactions t WHERE t.createdAt >= :from AND t.createdAt < :to AND t.cashFlow = :type")
    Long totalAmountBetweenAndType(@Param(value = "from") LocalDateTime from,
                                   @Param(value = "to") LocalDateTime to,
                                   @Param(value = "type") CashFlow type);

    @Query("SELECT t FROM Transactions t WHERE t.createdAt >= :from AND t.createdAt < :to AND t.cashFlow = :type ORDER BY t.createdAt DESC")
    List<Transactions> getLastedTransactionsBetweenByType(@Param(value = "from") LocalDateTime from,
                                                          @Param(value = "to") LocalDateTime to,
                                                          @Param(value = "type") CashFlow type, Pageable pageable);

}
