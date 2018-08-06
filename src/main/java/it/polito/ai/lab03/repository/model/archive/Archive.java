package it.polito.ai.lab03.repository.model.archive;

import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "archives")
public class Archive {

    @Id private String id;
    private String userId;
    private boolean onSale;
    @DBRef private List<Position> positions;
    @DBRef private List<Transaction> transactions;

    public Archive(String userId, List<Position> positions) {
        this.userId = userId;
        this.positions = positions;
        this.onSale = true;
        this.transactions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Archive)) return false;
        Archive archive = (Archive) o;
        return Objects.equals(id, archive.id) &&
                Objects.equals(userId, archive.userId) &&
                Objects.equals(positions, archive.positions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, positions);
    }
}
