package it.polito.ai.lab03.repository.model.archive;

import it.polito.ai.lab03.repository.model.position.Position;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "archives")
public class Archive {

    @Id
    private String id;
    private String userId;
    @DBRef private List<Position> positions;
    private boolean onSale;

    public Archive(String userId, List<Position> positions) {
        this.userId = userId;
        this.positions = positions;
        this.onSale = true;
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
