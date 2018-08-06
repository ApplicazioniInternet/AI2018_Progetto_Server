package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.archive.ArchiveLightList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Transaction buyArchives(ArchiveLightList archiveLightList, String buyerId) {
        //Costruzione della transazione (id autogenerato dal DB)
        Transaction transaction = new Transaction(buyerId, archiveLightList, 1, (System.currentTimeMillis() / 1000L));
        transactionRepository.insert(transaction);

        // segno in ogni archivio la lista di transaction in cui è stato acquistato
        archiveLightList.getArchiveList().forEach(
                archiveLight -> {
                    Archive archive = archiveRepository.findArchiveById(archiveLight.getArchiveId());
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
