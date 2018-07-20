package it.polito.ai.lab03.repository.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String buyerId;
    private String sellerId;

    private List<Position> boughtPositions;
    private double pricePaid;
    private double revenueForUser;

    public double getRevenueForUser() {
        return revenueForUser;
    }

    public void setRevenueForUser(double revenueForUser) {
        this.revenueForUser = revenueForUser;
    }

    private long timestamp;

    public Transaction(String buyerId, String sellerId, List<Position> boughtPositions, double pricePaid, double revenueForUser, long timestamp) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.boughtPositions = boughtPositions;
        this.pricePaid = pricePaid;
        this.timestamp = timestamp;
    }

    private String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    private String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<Position> getBoughtPositions() {
        return boughtPositions;
    }

    public void setBoughtPositions(List<Position> boughtPositions) {
        this.boughtPositions = boughtPositions;
    }

    private double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    private long getTimestamp() {
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
        return Double.compare(that.getPricePaid(), getPricePaid()) == 0 &&
                getTimestamp() == that.getTimestamp() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getBuyerId(), that.getBuyerId()) &&
                Objects.equals(getSellerId(), that.getSellerId()) &&
                Objects.equals(getBoughtPositions(), that.getBoughtPositions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBuyerId(), getSellerId(), getBoughtPositions(), getPricePaid(), getTimestamp());
    }
}
