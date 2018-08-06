package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.archive.ArchiveIdsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private ArchiveRepository archiveRepository;

    @Autowired
    public TransactionService(TransactionRepository tr,
                              ArchiveRepository ar) {
        this.transactionRepository = tr;
        this.archiveRepository = ar;
    }

    public List<Transaction> getTransactionsPerUser(String username) {
        return transactionRepository.findAllByBuyerId(username);
    }

    void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }


    public Transaction buyArchives(ArchiveIdsList archiveIdsList, String buyerId) {

        //Costruzione della transazione (id autogenerato dal DB)
        Transaction transaction = new Transaction(buyerId, archiveIdsList, 1, (System.currentTimeMillis() / 1000L));
        transactionRepository.insert(transaction);

        // segno in ogni archivio la lista di transaction in cui è stato acquistato
        archiveIdsList.getArchiveIds().forEach(
                archiveId -> {
                    Archive archive = archiveRepository.findArchiveById(archiveId.getArchiveId());
                    List<Transaction> transactions = archive.getTransactions();
                    if (transactions == null)
                        transactions = new ArrayList<>();
                    transactions.add(transaction);
                    archive.setTransactions(transactions);
                    archiveRepository.save(archive);
                }
        );

        return transaction;
    }
}
