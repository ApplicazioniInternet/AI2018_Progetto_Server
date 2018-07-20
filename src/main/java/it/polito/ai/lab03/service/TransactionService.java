package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository tr) {
        this.transactionRepository = tr;
    }

    public List<Transaction> getTransactionsPerUser(String username) {
        return transactionRepository.findAllByBuyerId(username);
    }

    void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }
}
