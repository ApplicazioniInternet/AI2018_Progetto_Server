package it.polito.ai.lab03.repository.model.transaction;

import it.polito.ai.lab03.repository.model.archive.ArchiveIdsList;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class Transaction {

    private String id;
    private String buyerId;
    private ArchiveIdsList archivesBought;
    private double pricePaid;
    private long timestamp;

    public Transaction(String buyerId, ArchiveIdsList archivesBought, double pricePaid, long timestamp) {
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

    public ArchiveIdsList getArchivesBought() {
        return archivesBought;
    }

    public void setArchivesBought(ArchiveIdsList archivesBought) {
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
