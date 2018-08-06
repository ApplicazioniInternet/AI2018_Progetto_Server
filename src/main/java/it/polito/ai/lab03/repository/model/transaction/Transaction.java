package it.polito.ai.lab03.repository.model.transaction;

import it.polito.ai.lab03.repository.model.archive.ArchiveLightList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class Transaction {

    @Id private String id;
    private String buyerId;
    private ArchiveLightList archivesBought;
    private double pricePaid;
    private long timestamp;

    public Transaction(String buyerId, ArchiveLightList archivesBought, double pricePaid, long timestamp) {
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

    public ArchiveLightList getArchivesBought() {
        return archivesBought;
    }

    public void setArchivesBought(ArchiveLightList archivesBought) {
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
