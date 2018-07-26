package it.polito.ai.lab03.repository.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String buyerId;
    private String sellerId;

    private Archive archiveBought;
    private double pricePaid;
    private double revenueForUser;

    public double getRevenueForUser() {
        return revenueForUser;
    }

    public void setRevenueForUser(double revenueForUser) {
        this.revenueForUser = revenueForUser;
    }

    private long timestamp;

    public Transaction(String buyerId, String sellerId, Archive archiveBought, double pricePaid, double revenueForUser, long timestamp) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.archiveBought = archiveBought;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Archive getArchiveBought() {
        return archiveBought;
    }

    public void setArchiveBought(Archive archiveBought) {
        this.archiveBought = archiveBought;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.pricePaid, pricePaid) == 0 &&
                Double.compare(that.revenueForUser, revenueForUser) == 0 &&
                timestamp == that.timestamp &&
                Objects.equals(id, that.id) &&
                Objects.equals(buyerId, that.buyerId) &&
                Objects.equals(sellerId, that.sellerId) &&
                Objects.equals(archiveBought, that.archiveBought);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buyerId, sellerId, archiveBought, pricePaid, revenueForUser, timestamp);
    }
}
