package it.polito.ai.lab03.repository.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "positionsReprTime")
public class PositionRepresentationTimestamp extends Position {

    private String positionId;

    public PositionRepresentationTimestamp(Position position) {
        super(
                position.getUserId(),
                position.getArchiveId(),
                Math.round( position.getTimestamp() / 100 ) * 100
        );
        this.positionId = position.getId();
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionRepresentationTimestamp)) return false;
        PositionRepresentationTimestamp that = (PositionRepresentationTimestamp) o;
        return Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positionId);
    }
}
