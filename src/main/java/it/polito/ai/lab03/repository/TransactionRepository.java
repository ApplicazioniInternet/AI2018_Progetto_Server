package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("ALL")
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByBuyerId(@NonNull String buyerId);
    List<Transaction> findAllByTimestampBefore(@NonNull long timestampBefore);
    List<Transaction> findByBuyerIdAndArchivesBoughtArchiveListContains(@NonNull String buyerId,
                                                             @NonNull ArchiveLight archiveId);
    Transaction insert(@NonNull Transaction transaction);
}

