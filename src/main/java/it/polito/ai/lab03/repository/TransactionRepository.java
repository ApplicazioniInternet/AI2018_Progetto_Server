package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("ALL")
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByBuyerId(@NonNull String buyerId);

    List<Transaction> findAllBySellerId(@NonNull String sellerId);

    List<Transaction> findAllByTimestampBefore(@NonNull long timestampBefore);

    Transaction insert(@NonNull Transaction transaction);
}
