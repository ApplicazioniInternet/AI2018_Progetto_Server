package it.polito.ai.lab03.repository.model;

import it.polito.ai.lab03.repository.model.archive.Archive;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private String buyerId;
    @DBRef private List<Archive> archivesBought;
    private double pricePaid;
    private long timestamp;

    public Transaction(String buyerId, List<Archive> archivesBought, double pricePaid, long timestamp) {
        this.buyerId = buyerId;
        this.archivesBought = archivesBought;
        this.pricePaid = pricePaid;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public List<Archive> getArchivesBought() {
        return archivesBought;
    }

    public void setArchivesBought(List<Archive> archivesBought) {
        this.archivesBought = archivesBought;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
