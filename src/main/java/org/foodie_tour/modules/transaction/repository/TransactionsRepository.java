package org.foodie_tour.modules.transaction.repository;

import org.foodie_tour.modules.transaction.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}
